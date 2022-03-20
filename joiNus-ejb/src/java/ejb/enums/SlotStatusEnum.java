/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.enums;
//package util.enumeration;

/**
 *
 * @author wongs
 */
public enum SlotStatusEnum {
    /*
    unavailable: someone else booked it
    available: no one has booked it yet
    cancelled: someone booked it but its cancelled
    */
    UNAVAILABLE,
    AVAILABLE,
    CANCELLED
}
