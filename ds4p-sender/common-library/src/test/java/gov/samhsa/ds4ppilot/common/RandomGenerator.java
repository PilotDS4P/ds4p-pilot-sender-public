/*******************************************************************************
 * Open Behavioral Health Information Technology Architecture (OBHITA.org)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package gov.samhsa.ds4ppilot.common;

import java.util.Random;

public class RandomGenerator {

	public static final String ALPHA_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String ALPHA_LOWER = "abcdefghijklmnopqrstuvwxyz";
	public static final String NUMERIC = "0123456789";
	public static final String ALPHA_NUMERIC_MIXED = ALPHA_LOWER + ALPHA_UPPER
			+ NUMERIC;
	static Random rnd = new Random();

	public static String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(ALPHA_NUMERIC_MIXED.charAt(rnd
					.nextInt(ALPHA_NUMERIC_MIXED.length())));
		return sb.toString();
	}

	public static String randomString() {
		int startLen = Integer.MAX_VALUE/1800;
		int len = rnd.nextInt(startLen) + 1;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++)
			sb.append(ALPHA_NUMERIC_MIXED.charAt(rnd
					.nextInt(ALPHA_NUMERIC_MIXED.length())));
		return sb.toString();
	}

	public static byte[] randomBytes(int len) {
		byte[] bytes = new byte[len];
		rnd.nextBytes(bytes);
		return bytes;
	}

	public static byte[] randomBytes() {
		byte[] bytes = new byte[randomInteger(254) + 1];
		rnd.nextBytes(bytes);
		return bytes;
	}

	public static char[] randomChars(int len) {
		return randomString(len).toCharArray();
	}

	protected static char[] randomChars() {
		return randomString().toCharArray();
	}

	protected static int randomInteger(int max) {
		return rnd.nextInt(max);
	}
}
