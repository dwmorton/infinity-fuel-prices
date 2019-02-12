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
import java.math.RoundingMode;

public class FuelPrice {

    private final BigDecimal pumpPencePerLitre;
    private final BigDecimal dutyPencePerLitre;
    private final BigDecimal vatFraction;

    public FuelPrice(BigDecimal pumpPencePerLitre, BigDecimal dutyPencePerLitre, BigDecimal vatPercentage) {
        this.pumpPencePerLitre = pumpPencePerLitre;
        this.dutyPencePerLitre = dutyPencePerLitre;
        this.vatFraction = vatPercentage.divide(BigDecimal.valueOf(100), 4, RoundingMode.UP);
    }

    public BigDecimal getPumpPencePerLitre() {
        return pumpPencePerLitre;
    }

    public BigDecimal getDutyPencePerLitre() {
        return dutyPencePerLitre;
    }

    public BigDecimal getVatFraction() {
        return vatFraction;
    }

    public BigDecimal grossPricePerLitre() {
        BigDecimal pumpPlusDuty = pumpPencePerLitre.add(dutyPencePerLitre);
        return addVAT(pumpPlusDuty);
    }

    public BigDecimal dutyPerLitreIncVAT() {
        return addVAT(dutyPencePerLitre);
    }

    BigDecimal addVAT(BigDecimal exVAT) {
        return exVAT.add(calculateVAT(exVAT));
    }

    BigDecimal calculateVAT(BigDecimal exVAT) {
        return exVAT.multiply(vatFraction);
    }

}
