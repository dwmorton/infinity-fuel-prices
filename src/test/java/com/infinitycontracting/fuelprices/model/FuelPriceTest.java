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

import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.comparesEqualTo;
import static org.junit.Assert.*;

public class FuelPriceTest {

    private final BigDecimal pumpPrice = BigDecimal.valueOf(100);
    private final BigDecimal duty = BigDecimal.valueOf(10);
    private final BigDecimal vatPercentage = BigDecimal.valueOf(10);

    private final FuelPrice fuelPrice = new FuelPrice(pumpPrice, duty, vatPercentage);

    @Test
    public void getPumpPencePerLitre() {
        assertThat(fuelPrice.getPumpPencePerLitre(), comparesEqualTo(pumpPrice));
    }

    @Test
    public void getDutyPencePerLitre() {
        assertThat(fuelPrice.getDutyPencePerLitre(), comparesEqualTo(duty));
    }

    @Test
    public void getVatFraction() {
        assertThat(fuelPrice.getVatFraction(), comparesEqualTo(BigDecimal.valueOf(0.1)));
    }

    @Test
    public void grossPricePerLitre() {
        assertThat(fuelPrice.grossPricePerLitre(), comparesEqualTo(BigDecimal.valueOf(121)));
    }

    @Test
    public void dutyPerLitreIncVAT() {
        assertThat(fuelPrice.dutyPerLitreIncVAT(), comparesEqualTo(BigDecimal.valueOf(11)));
    }

    @Test
    public void addVAT() {
        assertThat(fuelPrice.addVAT(BigDecimal.valueOf(100)), comparesEqualTo(BigDecimal.valueOf(110)));
    }

    @Test
    public void calculateVAT() {
        assertThat(fuelPrice.calculateVAT(BigDecimal.valueOf(100)), comparesEqualTo(BigDecimal.valueOf(10)));
    }
}