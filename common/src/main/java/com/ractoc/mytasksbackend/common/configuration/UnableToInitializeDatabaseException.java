/*
 *
 *  ---------------------------------------------------------------------------------------------------------
 *              Titel: UnableToInitializeDatabaseException.java
 *             Auteur: schrm01
 *    Creatietijdstip: 13 apr. 2018 14:02:55
 *          Copyright: (c) 2018 Belastingdienst / Centrum voor Applicatieontwikkeling en Onderhoud,
 *                     All Rights Reserved.
 *  ---------------------------------------------------------------------------------------------------------
 *                                              |   Unpublished work. This computer program includes
 *     De Belastingdienst                       |   Confidential, Properietary Information and is a
 *     Postbus 9050                             |   trade Secret of the Belastingdienst. No part of
 *     7300 GM  Apeldoorn                       |   this file may be reproduced or transmitted in any
 *     The Netherlands                          |   form or by any means, electronic or mechanical,
 *     http://belastingdienst.nl/               |   for the purpose, without the express written
 *                                              |   permission of the copyright holder.
 *  ---------------------------------------------------------------------------------------------------------
 *
 */
package com.ractoc.mytasksbackend.common.configuration;

import ch.vorburger.exec.ManagedProcessException;

class UnableToInitializeDatabaseException extends RuntimeException {

	private static final long serialVersionUID = -270096547353636378L;

	UnableToInitializeDatabaseException(ManagedProcessException e) {
		super(e);
	}

}
