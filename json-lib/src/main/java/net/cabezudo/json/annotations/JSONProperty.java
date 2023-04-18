/**
 * MIT License
 *
 * Copyright (c) 2017 Esteban Cabezudo
 *
 * Permission is hereby granted, free of charge, to any person who obtains a copy
 * of this software and it´s associated documentation files (the "Software"), to deal
 * in the Software without restriction or limitation of rights.
 * To use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit people to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE, IT´S USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.cabezudo.json.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Marks an object attribute like a property of a JSON object. You can also indicate with the
 * annotation the name you want for the field in the JSON object using the element {@code name}.
 * Also you can indicate if you want to show the property in the result JSON object if the property
 * value is zero or if the property is {@code null}. If the field will be a referenceable field you
 * can specify the field that be used like reference with the element {@code field}. Also you can
 * specify if the field can be referenced or not using the element {@code referenced}.
 *
 * @author Esteban Cabezudo
 * @version 0.9, 07/06/2016
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONProperty {

  /**
   * A string indicating if the name of the field will be the same for the JSON object property.
   */
  public static final String DEFAULT_NAME = "The same as the name of the property.";

  /**
   * The default property name for referenced fields.
   */
  public static final String DEFAULT_REFERENCED_PROPERTY = "id";

  /**
   * The name of the JSON object.
   *
   * @return a string indicating that the name of the JSON object property. If the method return
   * {@code DEFAULT_NAME} the library must use the field name.
   */
  String name() default DEFAULT_NAME;

  /**
   * Indicate if the property should be included in the JSON object if is zero or not.
   *
   * @return {@code true} if the property with null value will not show in the JSON object,
   * {@code false} otherwise.
   */
  boolean dontShowIfZero() default false;

  /**
   * Indicate if the property should be included in the JSON object if it is null or not.
   *
   * @return {@code true} if the property with null value will not show in the JSON object,
   * {@code false} otherwise.
   */
  boolean dontShowIfNull() default false;

  /**
   * Indicate if the property should be included in the JSON object if it is null or not.
   *
   * @return {@code true} if the property with empty value will not show in the JSON object,
   * {@code false} otherwise.
   */
  boolean dontShowIfEmpty() default false;

  /**
   *
   * Return the referenced field name used for an object in the annotated field. The default value
   * is {@code id}.
   *
   * @return the name of the field used like reference.
   */
  String field() default DEFAULT_REFERENCED_PROPERTY;

  /**
   *
   * Return if the field can be referenced or not. The default value is {code true}.
   *
   * @return {@code true} if the field is referenced {code false} otherwise.
   */
  boolean referenced() default true;
}
