package com.pigcms.library.android.okhttp.https;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Created by win7 on 2017-02-04.
 */

public class  UnSafeTrustManager implements X509TrustManager
{
    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException
    {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException
    {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
        return new X509Certificate[]{};
    }
}