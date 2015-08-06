/*
 * Copyright (c) 2007-2012 The Broad Institute, Inc.
 * SOFTWARE COPYRIGHT NOTICE
 * This software and its documentation are the copyright of the Broad Institute, Inc. All rights are reserved.
 *
 * This software is supplied without any warranty or guaranteed support whatsoever. The Broad Institute is not responsible for its use, misuse, or functionality.
 *
 * This software is licensed under the terms of the GNU Lesser General Public License (LGPL),
 * Version 2.1 which is available at http://www.opensource.org/licenses/lgpl-2.1.php.
 */

package org.broad.igv.sam;

import htsjdk.tribble.Feature;

/**
 * @author Jim Robinson
 * @date 11/22/11
 */
public interface AlignmentCounts extends Feature {

    void incCounts(Alignment alignment);

    int getTotalCount(int pos);

    int getTotalQuality(int pos);

    int getCount(int pos, byte b);

    int getNegCount(int pos, byte b);

    int getPosCount(int pos, byte b);

    int getDelCount(int pos);

    int getInsCount(int pos);

    int getQuality(int pos, byte b);

    int getNumberOfPoints();

    int getMaxCount(int origin, int end);

    String getValueStringAt(int pos);

    boolean isMismatch(int pos, byte ref, String chr, float snpThreshold);

    BisulfiteCounts getBisulfiteCounts();

    void finish();

}
