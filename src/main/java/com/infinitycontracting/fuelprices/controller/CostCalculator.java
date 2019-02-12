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

import com.infinitycontracting.fuelprices.model.FuelCost;
import com.infinitycontracting.fuelprices.model.FuelPrice;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class CostCalculator implements CostCalculatorInterface {

    @Override
    public FuelCost calculateFuelCost(FuelPrice fuelPriceOnDate, FuelPrice fuelPriceToday, BigDecimal milesPerGallon, BigDecimal mileage) {

        BigDecimal gallonsUsed = gallonsUsed(milesPerGallon, mileage);
        BigDecimal litresUsed  = gallonsToLitres(gallonsUsed);

        BigDecimal costOnDate = fuelPriceOnDate.grossPricePerLitre().multiply(litresUsed);
        BigDecimal dutyCostOnDate = fuelPriceOnDate.dutyPerLitreIncVAT().multiply(litresUsed);
        BigDecimal costToday = fuelPriceToday.grossPricePerLitre().multiply(litresUsed);

        return new FuelCost(costOnDate, dutyCostOnDate, costToday.subtract(costOnDate));
    }

    @Override
    public BigDecimal gallonsUsed(BigDecimal milesPerGallon, BigDecimal mileage) {
        return mileage.divide(milesPerGallon, 3, RoundingMode.UP);
    }

    @Override
    public BigDecimal litresToGallons(BigDecimal litres) {
        return litres.divide(litresPerGallon, 6, RoundingMode.UP);
    }

    @Override
    public BigDecimal gallonsToLitres(BigDecimal gallons) {
        return gallons.multiply(litresPerGallon);
    }

}
