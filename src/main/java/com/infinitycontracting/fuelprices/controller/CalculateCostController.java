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

package com.infinitycontracting.fuelprices.controller;

import com.infinitycontracting.fuelprices.exceptions.DateTooEarlyException;
import com.infinitycontracting.fuelprices.exceptions.InvalidDateException;
import com.infinitycontracting.fuelprices.exceptions.InvalidFuelTypeException;
import com.infinitycontracting.fuelprices.exceptions.InvalidInputNumberException;
import com.infinitycontracting.fuelprices.model.FuelCost;
import com.infinitycontracting.fuelprices.model.FuelPrice;
import com.infinitycontracting.fuelprices.model.FuelType;
import com.infinitycontracting.fuelprices.service.PriceServiceInterface;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
public class CalculateCostController {

    private final PriceServiceInterface priceService;
    private final CostCalculatorInterface costCalculator;

    @Inject
    public CalculateCostController(PriceServiceInterface priceService, CostCalculatorInterface costCalculator) {
        this.priceService = priceService;
        this.costCalculator = costCalculator;
    }

    @RequestMapping("/getFuelCost")
    FuelCost getFuelCost(
            @RequestParam String date,
            @RequestParam String fuelType,
            @RequestParam String milesPerGallon,
            @RequestParam String mileage) throws DateTooEarlyException, InvalidFuelTypeException {

        return getFuelCost(
                parseDateOrThrowException(date),
                parseFuelTypeOrThrowException(fuelType),
                parseBigDecimalOrThrowException(milesPerGallon),
                parseBigDecimalOrThrowException(mileage));
    }

    private FuelCost getFuelCost(LocalDate date, FuelType fuelType, BigDecimal milesPerGallon, BigDecimal mileage) throws DateTooEarlyException, InvalidFuelTypeException {

        FuelPrice fuelPriceOnDate = priceService.getFuelPrice(date, fuelType);
        FuelPrice fuelPriceToday = priceService.getCurrentFuelPrice(fuelType);

        return costCalculator.calculateFuelCost(fuelPriceOnDate, fuelPriceToday, milesPerGallon, mileage);
    }

    BigDecimal parseBigDecimalOrThrowException(String s) throws InvalidFuelTypeException {

        BigDecimal bd = BigDecimal.ZERO;

        try {
            bd = new BigDecimal(s);
        } catch(IllegalArgumentException e) {
            throw new InvalidInputNumberException(s);
        }

        if (!(bd.compareTo(BigDecimal.ZERO) > 0)  ) {
            throw new InvalidInputNumberException(s);
        }

        return bd;
    }
    LocalDate parseDateOrThrowException(String s) throws InvalidFuelTypeException {

        try {
            return LocalDate.parse(s);
        } catch(DateTimeParseException e) {
            throw new InvalidDateException(s);
        }
    }

    FuelType parseFuelTypeOrThrowException(String s) throws InvalidFuelTypeException {

        try {
            return FuelType.valueOf(s);
        } catch(IllegalArgumentException e) {
            throw new InvalidFuelTypeException(s);
        }
    }
}
