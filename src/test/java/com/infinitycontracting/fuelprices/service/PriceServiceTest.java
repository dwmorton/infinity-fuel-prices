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

package com.infinitycontracting.fuelprices.service;

import com.infinitycontracting.fuelprices.data.FuelPriceDataSource;
import com.infinitycontracting.fuelprices.model.FuelPrice;
import com.infinitycontracting.fuelprices.model.FuelType;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PriceServiceTest {

    private FuelPriceDataSource mockDataSource = mock(FuelPriceDataSource.class);

    private PriceServiceInterface priceService = new PriceService(mockDataSource);
    private FuelPrice fuelPrice = new FuelPrice(BigDecimal.ONE, BigDecimal.ONE,BigDecimal.ONE);
    private FuelPrice currentFuelPrice = new FuelPrice(BigDecimal.ONE, BigDecimal.ONE,BigDecimal.ONE);

    @Before
    public void setUp() {
        when(mockDataSource.getFuelPrice(any(LocalDate.class), any(FuelType.class))).thenReturn(fuelPrice);
        when(mockDataSource.getCurrentFuelPrice(any(FuelType.class))).thenReturn(currentFuelPrice);
    }

    @Test
    public void getFuelPrice() {
        assertThat(priceService.getFuelPrice(LocalDate.of(2019, 1, 1), FuelType.ULSP), equalTo(fuelPrice));
    }

    @Test
    public void getCurrentFuelPrice() {
        assertThat(priceService.getCurrentFuelPrice(FuelType.ULSD), equalTo(currentFuelPrice));
    }
}