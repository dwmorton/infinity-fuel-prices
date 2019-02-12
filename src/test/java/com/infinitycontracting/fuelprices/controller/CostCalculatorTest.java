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
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CostCalculatorTest {

    private CostCalculatorInterface costCalculator = new CostCalculator();

    private final BigDecimal pumpPriceOnDay = BigDecimal.valueOf(100);
    private final BigDecimal dutyOnDay = BigDecimal.valueOf(50);
    private final BigDecimal vatPercentageOnDay = BigDecimal.valueOf(17.5);
    private final BigDecimal vatFractionOnDay = BigDecimal.valueOf(0.175);


    private final BigDecimal pumpPriceToday = BigDecimal.valueOf(110);
    private final BigDecimal dutyToday = BigDecimal.valueOf(60);
    private final BigDecimal vatPercentageToday = BigDecimal.valueOf(20);
    private final BigDecimal vatFractionToday = BigDecimal.valueOf(0.20);

    private BigDecimal litresPerGallon = new BigDecimal("4.54609");

    private BigDecimal litres = new BigDecimal("4.54609");
    private BigDecimal gallons = BigDecimal.ONE;
    private BigDecimal milesPerGallon = BigDecimal.valueOf(25);
    private BigDecimal mileage = BigDecimal.valueOf(100);

    @Test
    public void calculatePrice01() {

        FuelPrice priceOnDay = new FuelPrice(pumpPriceOnDay, dutyOnDay, vatPercentageOnDay);
        FuelPrice priceToday = new FuelPrice(pumpPriceToday, dutyToday, vatPercentageToday);

        //Calculate expected values
        BigDecimal ppl = pumpPriceOnDay.add(dutyOnDay).multiply(BigDecimal.ONE.add(vatFractionOnDay));
        BigDecimal pplNow = pumpPriceToday.add(dutyToday).multiply(BigDecimal.ONE.add(vatFractionToday));
        BigDecimal ll = (mileage.divide(milesPerGallon,3, RoundingMode.UP)).multiply(litresPerGallon);
        BigDecimal expectedFuelCostOnDay = ppl.multiply(ll);
        BigDecimal expectedFuelCostToday = pplNow.multiply(ll);
        BigDecimal expectedDelta = expectedFuelCostToday.subtract(expectedFuelCostOnDay);
        BigDecimal expectedDuty = dutyOnDay.multiply(BigDecimal.ONE.add(vatFractionOnDay)).multiply(ll);

        FuelCost fuelCost = costCalculator.calculateFuelCost(priceOnDay, priceToday, milesPerGallon, mileage);

        assertThat(fuelCost.getFuelCost(), equalTo(expectedFuelCostOnDay.intValue()));
        assertThat(fuelCost.getDutyPaid(), equalTo(expectedDuty.intValue()));
        assertThat(fuelCost.getDeltaToday(), equalTo(expectedDelta.intValue()));
    }

    @Test
    public void testLitresToGallons() {
        assertThat(costCalculator.litresToGallons(litres), comparesEqualTo(gallons));
    }

    @Test
    public void testGallonsToLitres() {
        assertThat(costCalculator.gallonsToLitres(gallons), comparesEqualTo(litres));
    }

    @Test
    public void testGallonsUsed() {
        assertThat(costCalculator.gallonsUsed(BigDecimal.valueOf(25), BigDecimal.valueOf(100)), comparesEqualTo(BigDecimal.valueOf(4)));
        assertThat(costCalculator.gallonsUsed(BigDecimal.valueOf(25), BigDecimal.valueOf(25)), comparesEqualTo(BigDecimal.ONE));
        assertThat(costCalculator.gallonsUsed(BigDecimal.valueOf(25), BigDecimal.ZERO), comparesEqualTo(BigDecimal.ZERO));
    }
}