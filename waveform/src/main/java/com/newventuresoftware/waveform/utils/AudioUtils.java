/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.newventuresoftware.waveform.utils;

import java.math.BigInteger;

public final class AudioUtils {
    public static int calculateAudioLength(int samplesCount, int sampleRate, int channelCount) {

        BigInteger miliseconds = BigInteger.valueOf(1000);
        BigInteger samples = BigInteger.valueOf(samplesCount);
        BigInteger channels = BigInteger.valueOf(channelCount);
        BigInteger rate = BigInteger.valueOf(sampleRate);

        BigInteger aux = (samples.divide(channels)).multiply(miliseconds);
        BigInteger resultado = aux.divide(rate);

        return resultado.intValue();
    }
}
