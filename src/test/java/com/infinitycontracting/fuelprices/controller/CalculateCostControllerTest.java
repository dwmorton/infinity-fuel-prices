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

import com.infinitycontracting.fuelprices.exceptions.InvalidDateException;
import com.infinitycontracting.fuelprices.exceptions.InvalidFuelTypeException;
import com.infinitycontracting.fuelprices.exceptions.InvalidInputNumberException;
import com.infinitycontracting.fuelprices.model.FuelCost;
import com.infinitycontracting.fuelprices.model.FuelPrice;
import com.infinitycontracting.fuelprices.model.FuelType;
import com.infinitycontracting.fuelprices.service.PriceServiceInterface;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CalculateCostControllerTest {

    private PriceServiceInterface mockPriceService = mock(PriceServiceInterface.class);
    private CostCalculatorInterface mockCostCalculator = mock(CostCalculatorInterface.class);
    private CalculateCostController controller = new CalculateCostController(mockPriceService, mockCostCalculator);

    private FuelPrice zeroFuelPrice = new FuelPrice(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);
    private FuelCost zeroFuelCost = new FuelCost(BigDecimal.ZERO,BigDecimal.ZERO,BigDecimal.ZERO);

    @Before
    public void setUp() {
        when(mockPriceService.getFuelPrice(any(LocalDate.class), any(FuelType.class))).thenReturn(zeroFuelPrice);
        when(mockPriceService.getCurrentFuelPrice(any(FuelType.class))).thenReturn(zeroFuelPrice);
        when(mockCostCalculator.calculateFuelCost(any(FuelPrice.class),any(FuelPrice.class),any(BigDecimal.class),any(BigDecimal.class))).thenReturn(zeroFuelCost);
    }

    @Test
    public void testGetFuelPricesWithStrings() {
        FuelCost cost = controller.getFuelCost("2019-01-31", "ULSP", "25", "100");
        assertThat(cost, equalTo(zeroFuelCost));
    }

    @Test(expected = InvalidDateException.class)
    public void testDateParsing_error_garbage() throws InvalidDateException {
        controller.parseDateOrThrowException("garbage");
    }

    @Test
    public void testDateParsing() {
        controller.parseDateOrThrowException("2012-12-31");
    }

    @Test(expected = InvalidInputNumberException.class)
    public void testNumberParsing_negative_number() throws InvalidInputNumberException {
        controller.parseBigDecimalOrThrowException("-0.002");
    }

    @Test(expected = InvalidInputNumberException.class)
    public void testNumberParsing_zero_input() throws InvalidInputNumberException {
        controller.parseBigDecimalOrThrowException("0");
    }

    @Test(expected = InvalidInputNumberException.class)
    public void testNumberParsing_garbage() throws InvalidInputNumberException {
        controller.parseBigDecimalOrThrowException("bananabananabanana");
    }

    @Test
    public void testFuelTypeParsing_invalid_fueltype() {
        for (FuelType ft : FuelType.values()) {
            assertThat(controller.parseFuelTypeOrThrowException(ft.name()), equalTo(ft));
        }
    }

    @Test(expected = InvalidFuelTypeException.class)
    public void testDateParsing_invalid_fueltype() throws InvalidFuelTypeException {
        controller.parseFuelTypeOrThrowException("blahblahblah");
    }
}