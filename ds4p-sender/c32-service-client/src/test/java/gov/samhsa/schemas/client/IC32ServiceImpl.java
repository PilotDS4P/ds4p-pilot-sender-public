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
package gov.samhsa.schemas.client;

import gov.samhsa.schemas.c32service.IC32Service;

import java.util.logging.Logger;

/**
 * This class was generated by Apache CXF 2.6.0 2012-09-06T08:28:05.248-04:00
 * Generated source version: 2.6.0
 * 
 */

@javax.jws.WebService(serviceName = "C32Service", portName = "BasicHttpBinding_IC32Service", targetNamespace = "http://schemas.samhsa.gov/c32service", wsdlLocation = "C32Service.wsdl", endpointInterface = "gov.samhsa.schemas.c32service.IC32Service")
public class IC32ServiceImpl implements IC32Service {

	private static final Logger LOG = Logger.getLogger(IC32ServiceImpl.class
			.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.samhsa.schemas.c32service.IC32Service#getC32(java.lang.String
	 * patientId )*
	 */
	@Override
	public java.lang.String getC32(java.lang.String patientId) {
		LOG.info("Executing operation getC32");
		System.out.println(patientId);
		final String returnedC32 = returnedValueOfGetC32;
		try {
			java.lang.String _return = returnedC32;
			return _return;
		} catch (java.lang.Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	public static String returnedValueOfGetC32 = "";

}
