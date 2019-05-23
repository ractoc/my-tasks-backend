package com.ractoc.mytasksbackend.common.service;

public class NoSuchEntryException extends ServiceException {
	private static final long serialVersionUID = 1L;

	public NoSuchEntryException(String type, String id) {
		super(type + " with " + id + " not found.");
	}
}
