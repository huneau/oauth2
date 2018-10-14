package com.huneau;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum GrantType {
	password,
	authorization_code,
	client_credentials,
	refresh_token;


}
