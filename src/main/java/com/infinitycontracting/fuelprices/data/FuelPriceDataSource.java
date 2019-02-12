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
import com.infinitycontracting.fuelprices.model.FuelPrice;
import com.infinitycontracting.fuelprices.model.FuelType;

import java.time.LocalDate;

public interface FuelPriceDataSource {

    FuelPrice getFuelPrice(LocalDate date, FuelType fuelType) throws DateTooEarlyException;
    FuelPrice getCurrentFuelPrice(FuelType fuelType);
}
