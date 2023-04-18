package net.cabezudo.json.objects;

import net.cabezudo.json.annotations.JSONProperty;
import net.cabezudo.json.values.JSONNumber;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="http://cabezudo.net">Esteban Cabezudo</a>
 * @version 0.9, 08/04/2016
 */
public class Types {

  @JSONProperty // 0
  private final BigDecimal bigDecimal = new BigDecimal("15.4", MathContext.DECIMAL32).setScale(JSONNumber.DEFAULT_SCALE).stripTrailingZeros();

  @JSONProperty // 1
  private final BigInteger bigInteger = new BigInteger("14");

  @JSONProperty // 2
  private final Book book = new Book(1, "Evolution");

  @JSONProperty // 3
  private final byte[] byteArray = {1, 2, 3, 4};

  @JSONProperty // 4
  private final Calendar calendar = Calendar.getInstance();

  @JSONProperty // 5
  private final Date date;

  @JSONProperty // 6
  private final Object nullReference = null;

  @JSONProperty // 7
  private final Boolean oBoolean = true;

  @JSONProperty // 8
  private final Byte oByte = 2;

  @JSONProperty // 9
  private final Character oCharacter = 'b';

  @JSONProperty // 10
  private final Double oDouble = 12.5d;

  @JSONProperty // 11
  private final Float oFloat = 10.5f;

  @JSONProperty // 12
  private final Integer oInteger = 6;

  @JSONProperty // 13
  private final Long oLong = 8l;

  @JSONProperty // 14
  private final Short oShort = 4;

  @JSONProperty // 15
  private final boolean pBoolean = false;

  @JSONProperty // 16
  private final byte pByte = 1;

  @JSONProperty // 17
  private final char pCharacter = 'a';

  @JSONProperty // 18
  private final double pDouble = 11.5d;

  @JSONProperty // 19
  private final float pFloat = 9.5f;

  @JSONProperty // 20
  private final int pInteger = 5;

  @JSONProperty // 21
  private final long pLong = 7;

  @JSONProperty // 22
  private final short pShort = 3;

  @JSONProperty // 23
  private final String string = "Esteban";

  public Types() {
    calendar.set(1974, 0, 30, 14, 20, 12);
    calendar.set(Calendar.MILLISECOND, 125);
    date = calendar.getTime();
  }

  public BigDecimal getBigDecimal() {
    return bigDecimal;
  }

  public BigInteger getBigInteger() {
    return bigInteger;
  }

  public Book getBook() {
    return book;
  }

  public byte[] getByteArray() {
    return byteArray;
  }

  public Calendar getCalendar() {
    return calendar;
  }

  public Date getDate() {
    return date;
  }

  public Object getNullReference() {
    return nullReference;
  }

  public Byte getOByte() {
    return oByte;
  }

  public Character getOCharacter() {
    return oCharacter;
  }

  public Double getODouble() {
    return oDouble;
  }

  public Float getOFloat() {
    return oFloat;
  }

  public Integer getOInteger() {
    return oInteger;
  }

  public Long getOLong() {
    return oLong;
  }

  public Short getOShort() {
    return oShort;
  }

  public byte getPByte() {
    return pByte;
  }

  public char getPCharacter() {
    return pCharacter;
  }

  public double getPDouble() {
    return pDouble;
  }

  public float getPFloat() {
    return pFloat;
  }

  public int getPInteger() {
    return pInteger;
  }

  public long getPLong() {
    return pLong;
  }

  public short getPShort() {
    return pShort;
  }

  public String getString() {
    return string;
  }

  public Boolean isOBoolean() {
    return oBoolean;
  }

  public boolean isPBoolean() {
    return pBoolean;
  }

}
