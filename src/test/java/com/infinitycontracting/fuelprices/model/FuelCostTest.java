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

package com.infinitycontracting.fuelprices.model;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class FuelCostTest {

    private FuelCost fuelCost;

    @Before
    public void setUp() {
        fuelCost = new FuelCost(BigDecimal.TEN, BigDecimal.ZERO, BigDecimal.ONE);
    }

    @Test
    public void testFuelCost() {
        assertThat(fuelCost.getFuelCost(), equalTo(10));
    }

    @Test
    public void testDutyPaid() {
        assertThat(fuelCost.getDutyPaid(), equalTo(0));
    }

    @Test
    public void testDeltaToday() {
        assertThat(fuelCost.getDeltaToday(), equalTo(1));
    }

}