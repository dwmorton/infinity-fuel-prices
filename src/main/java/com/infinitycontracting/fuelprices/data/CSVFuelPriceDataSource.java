/*
 *   Copyright (C) 2019 David W. Morton (david@infinitycontracting.com)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.infinitycontracting.fuelprices.data;

import com.infinitycontracting.fuelprices.exceptions.DateTooEarlyException;
import com.infinitycontracting.fuelprices.exceptions.InvalidPriceData;
import com.infinitycontracting.fuelprices.model.FuelPrice;
import com.infinitycontracting.fuelprices.model.FuelType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Logger;

@Component
public class CSVFuelPriceDataSource implements FuelPriceDataSource {

    private final static Logger LOGGER = Logger.getLogger(CSVFuelPriceDataSource.class.getName());

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String REGEX = "^.*values=\\[\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d, ([0-9]*\\.?[0-9]*, ){6}.*$";
    private final NavigableMap<LocalDate, Map<FuelType,FuelPrice>> fuelPriceDataMap = new TreeMap<>();

    @Autowired
    public CSVFuelPriceDataSource(@Value("${csvPriceData}") Resource csvPriceData) {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(csvPriceData.getInputStream()))) {

            CSVFormat.DEFAULT.parse(in).forEach(this::parseCSVLine);

        } catch (IOException | IllegalStateException e) {
            String msg = "Failed to open priceData.csv file '"+csvPriceData.getFilename() +"'";
            LOGGER.severe(msg);
            throw new InvalidPriceData(msg, e);
        }

        if (fuelPriceDataMap.isEmpty()) {
            String msg = "No valid data found in supplied CSV file";
            LOGGER.severe(msg);
            throw new InvalidPriceData(msg);
        }
    }

    private void parseCSVLine(CSVRecord r) {
        if (r.toString().matches(REGEX)) {
            parseCSVLine(r.iterator(), r.getRecordNumber());
        }
    }

    void parseCSVLine(Iterator<String> iterator, long lineNumber) {
        try {
            String dateStr = iterator.next();
            String pumpPricePetrol = iterator.next();
            String pumpPriceDiesel = iterator.next();
            String dutyPetrol = iterator.next();
            String dutyDiesel = iterator.next();
            String vatPetrol = iterator.next();
            String vatDiesel = iterator.next();

            LocalDate date = LocalDate.parse(dateStr, dateTimeFormatter);
            FuelPrice petrolPrice = new FuelPrice(new BigDecimal(pumpPricePetrol), new BigDecimal(dutyPetrol), new BigDecimal(vatPetrol));
            FuelPrice dieselPrice = new FuelPrice(new BigDecimal(pumpPriceDiesel), new BigDecimal(dutyDiesel), new BigDecimal(vatDiesel));

            Map<FuelType, FuelPrice> fuelTypePriceMap = new HashMap<>(FuelType.values().length);
            fuelTypePriceMap.put(FuelType.ULSP, petrolPrice);
            fuelTypePriceMap.put(FuelType.ULSD, dieselPrice);
            fuelPriceDataMap.put(date, fuelTypePriceMap);

        } catch (DateTimeParseException dtpe) {
            LOGGER.info("Invalid date in CSV file line "+lineNumber);

        } catch (NumberFormatException nfe) {
            LOGGER.info("Invalid number in CSV file line "+lineNumber);
        }
    }

    @Override
    public FuelPrice getFuelPrice(LocalDate date, FuelType fuelType) throws DateTooEarlyException {

        LocalDate previousDate = fuelPriceDataMap.floorKey(date);
        if (null == previousDate) {
            throw new DateTooEarlyException(date, fuelPriceDataMap.firstKey());
        }

        return fuelPriceDataMap.get(previousDate).get(fuelType);
    }

    @Override
    public FuelPrice getCurrentFuelPrice(FuelType fuelType) {
        return fuelPriceDataMap.lastEntry().getValue().get(fuelType);
    }
}
