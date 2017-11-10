# PEMUtilities Requirements

## Overview

Utility class for converting PEM formatted strings into public X509 certificates and private keys.

## References

## Exposed API

```java
public final class Base64
{
    public static PrivateKey parsePrivateKey(String privateKeyString) throws CertificateException;
    public static X509Certificate parsePublicKey(String publicKeyCertificateString) throws CertificateException;
}
```

### parsePrivateKey
```java
public static PrivateKey parsePrivateKey(String privateKeyString) throws CertificateException;
```

**SRS_PEMUTILITIES_21_001: [**This function shall return a Private Key instance created by the provided PEM formatted privateKeyString.**]**  

**SRS_PEMUTILITIES_21_002: [**If any exception is encountered while attempting to create the private key instance, this function shall throw a CertificateException.**]**  


### parsePublicKeyCertificate()
```java
public static X509Certificate parsePublicKeyCertificate(String publicKeyCertificateString) throws CertificateException;
```

**SRS_PEMUTILITIES_21_003: [**This function shall return an X509Certificate instance created by the provided PEM formatted publicKeyCertificateString.**]**  

**SRS_PEMUTILITIES_21_004: [**If any exception is encountered while attempting to create the public key certificate instance, this function shall throw a CertificateException.**]**  
