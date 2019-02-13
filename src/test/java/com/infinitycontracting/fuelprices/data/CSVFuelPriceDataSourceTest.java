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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@PropertySource("classpath:test.properties")
public class CSVFuelPriceDataSourceTest {

    @Autowired
    FuelPriceDataSource dataSource;

    @Test(expected = DateTooEarlyException.class)
    public void getFuelPricePetrol_TooEarly() throws DateTooEarlyException {
        FuelPrice price = dataSource.getFuelPrice(LocalDate.of(1970,1,1), FuelType.ULSP);
        assertThat(price.getPumpPencePerLitre(), comparesEqualTo(new BigDecimal("129.18")));
        assertThat(price.getDutyPencePerLitre(), comparesEqualTo(new BigDecimal("57.95")));
        assertThat(price.getVatFraction(), comparesEqualTo(new BigDecimal("0.2")));
    }

    @Test
    public void getFuelPricePetrol_WithNoMatchingDate() throws DateTooEarlyException {
        FuelPrice price = dataSource.getFuelPrice(LocalDate.of(2018,9,1), FuelType.ULSP);
        assertThat(price.getPumpPencePerLitre(), comparesEqualTo(new BigDecimal("129.18")));
        assertThat(price.getDutyPencePerLitre(), comparesEqualTo(new BigDecimal("57.95")));
        assertThat(price.getVatFraction(), comparesEqualTo(new BigDecimal("0.2")));
    }

    @Test
    public void getFuelPricePetrol_Future() throws DateTooEarlyException {
        FuelPrice price = dataSource.getFuelPrice(LocalDate.of(2050,1,1), FuelType.ULSP);
        assertThat(price.getPumpPencePerLitre(), comparesEqualTo(new BigDecimal("119.13")));
        assertThat(price.getDutyPencePerLitre(), comparesEqualTo(new BigDecimal("57.95")));
        assertThat(price.getVatFraction(), comparesEqualTo(new BigDecimal("0.2")));
    }

    @Test
    public void getCurrentPetrolPrice() {
        FuelPrice price = dataSource.getCurrentFuelPrice(FuelType.ULSP);
        assertThat(price.getPumpPencePerLitre(), comparesEqualTo(new BigDecimal("119.13")));
        assertThat(price.getDutyPencePerLitre(), comparesEqualTo(new BigDecimal("57.95")));
        assertThat(price.getVatFraction(), comparesEqualTo(new BigDecimal("0.2")));
    }

    @Test
    public void getCurrentDieselPrice() {
        FuelPrice price = dataSource.getCurrentFuelPrice(FuelType.ULSD);
        assertThat(price.getPumpPencePerLitre(), comparesEqualTo(new BigDecimal("129.13")));
        assertThat(price.getDutyPencePerLitre(), comparesEqualTo(new BigDecimal("57.95")));
        assertThat(price.getVatFraction(), comparesEqualTo(new BigDecimal("0.2")));
    }

    @Test
    public void parseInvalidDateLine() {
        CSVFuelPriceDataSource csvDataSource = (CSVFuelPriceDataSource) dataSource;
        csvDataSource.parseCSVLine(Arrays.asList("99/00/1970,0,0,0,0,0,0".split(",")).iterator(),5);
    }

    @Test
    public void parseInvalidNumberLine() {
        CSVFuelPriceDataSource csvDataSource = (CSVFuelPriceDataSource) dataSource;
        csvDataSource.parseCSVLine(Arrays.asList("01/01/2019,0,0,0,0,0,badNumber".split(",")).iterator(),5);
    }

    @Test(expected = InvalidPriceData.class)
    public void parseMissingFile() throws InvalidPriceData, IOException {

        Resource mockResource = mock(Resource.class);
        InputStream mockInputStream = mock(InputStream.class);
        when(mockResource.getInputStream()).thenReturn(mockInputStream);

        new CSVFuelPriceDataSource(mockResource);
    }

    @Test(expected = InvalidPriceData.class)
    public void parseFileWithNoValidData() throws InvalidPriceData, IOException {

        Resource mockResource = mock(Resource.class);
        when(mockResource.getInputStream()).thenReturn(new ByteArrayInputStream("1,1,1,1\n2,2,2,2\n".getBytes()));

        new CSVFuelPriceDataSource(mockResource);
    }

}