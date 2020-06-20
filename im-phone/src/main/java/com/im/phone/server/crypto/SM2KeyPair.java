package com.im.phone.server.crypto;


import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * @description:SM2密钥对Bean
 * @author: shiqilong / jackSmail
 * @create: 2020-02-04 10:40
 */
public class SM2KeyPair {

	public final ECPoint publicKey;
	public final BigInteger privateKey;

	public SM2KeyPair(ECPoint publicKey, BigInteger privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	public ECPoint getPublicKey() {
		return publicKey;
	}

	public BigInteger getPrivateKey() {
		return privateKey;
	}
}
