/*
*  Copyright (c) Microsoft. All rights reserved.
*  Licensed under the MIT license. See LICENSE file in the project root for full license information.
*/

package tests.unit.com.microsoft.azure.sdk.iot.deps.util;

import com.microsoft.azure.sdk.iot.deps.util.PemUtilities;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import static org.junit.Assert.assertEquals;

public class PemUtilitiesTest
{
    private static final String expectedPrivateKeyString = "some private key string";
    private static final String expectedPublicKeyCertificateString = "some public key certificate string";

    @Mocked PrivateKey mockPrivateKey;
    @Mocked X509Certificate mockX509Certificate;
    @Mocked PEMKeyPair mockPEMKeyPair;
    @Mocked PEMParser mockPEMParser;
    @Mocked JcaPEMKeyConverter mockJcaPEMKeyConverter;
    @Mocked PemObject mockPemObject;
    @Mocked PemReader mockPemReader;
    @Mocked StringReader mockStringReader;
    @Mocked KeyPair mockKeyPair;

    // Tests_SRS_PEMUTILITIES_21_001: [This function shall return a Private Key instance created by the provided PEM formatted privateKeyString.]
    @Test
    public void parsePrivateKeySuccess() throws CertificateException, IOException
    {
        //arrange
        new NonStrictExpectations()
        {
            {
                new StringReader(expectedPrivateKeyString);
                result = mockStringReader;

                new PEMParser(mockStringReader);
                result = mockPEMParser;

                mockPEMParser.readObject();
                result = mockPEMKeyPair;

                new JcaPEMKeyConverter();
                result = mockJcaPEMKeyConverter;

                mockJcaPEMKeyConverter.getKeyPair(mockPEMKeyPair);
                result = mockKeyPair;

                mockKeyPair.getPrivate();
                result = mockPrivateKey;
            }
        };

        //act
        PrivateKey actualPrivateKey = PemUtilities.parsePrivateKey(expectedPrivateKeyString);

        //assert
        assertEquals(mockPrivateKey, actualPrivateKey);
    }

    // Tests_SRS_PEMUTILITIES_21_002: [If any exception is encountered while attempting to create the private key instance, this function shall throw a CertificateException.]

    // Tests_SRS_PEMUTILITIES_21_003: [This function shall return an X509Certificate instance created by the provided PEM formatted publicKeyCertificateString.]
    @Test
    public void parsePublicKeyCertificateSuccess() throws CertificateException
    {
        //arrange
        new NonStrictExpectations()
        {
            {

            }
        };

        //act
        X509Certificate actualPublicKeyCertificate = PemUtilities.parsePublicKeyCertificate(expectedPublicKeyCertificateString);

        //assert
        assertEquals(mockX509Certificate, actualPublicKeyCertificate);
    }

    // Tests_SRS_PEMUTILITIES_21_004: [If any exception is encountered while attempting to create the public key certificate instance, this function shall throw a CertificateException.]

}
