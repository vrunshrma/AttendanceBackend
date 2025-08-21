package com.cedrus.attendance.request;

import lombok.Data;

@Data
public class APIRequest<T> {

	private T request;
}
