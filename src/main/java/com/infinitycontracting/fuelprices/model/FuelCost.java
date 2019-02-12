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

import java.math.BigDecimal;

public class FuelCost {
    private final int fuelCost;
    private final int dutyPaid;
    private final int deltaToday;

    public FuelCost(BigDecimal fuelCost, BigDecimal dutyPaid, BigDecimal deltaToday) {
        this.fuelCost = fuelCost.intValue();
        this.dutyPaid = dutyPaid.intValue();
        this.deltaToday = deltaToday.intValue();
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public int getDutyPaid() {
        return dutyPaid;
    }

    public int getDeltaToday() {
        return deltaToday;
    }
}
