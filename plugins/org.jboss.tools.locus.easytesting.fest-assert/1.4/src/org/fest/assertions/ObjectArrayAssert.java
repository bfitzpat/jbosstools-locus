/*
 * Created on Mar 3, 2007
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2007-2011 the original author or authors.
 */
package org.fest.assertions;

import static org.fest.assertions.ErrorMessages.*;
import static org.fest.assertions.Formatting.format;
import static org.fest.util.Collections.*;

import java.util.*;

import org.fest.util.IntrospectionError;

/**
 * Assertions for {@code Object} arrays.
 * <p>
 * To create a new instance of this class invoke <code>{@link Assertions#assertThat(Object[])}</code>.
 * </p>
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ObjectArrayAssert extends ObjectGroupAssert<ObjectArrayAssert, Object[]> {

  /**
   * Creates a new </code>{@link ObjectArrayAssert}</code>.
   * @param actual the target to verify.
   */
  protected ObjectArrayAssert(Object... actual) {
    super(ObjectArrayAssert.class, actual);
  }

  /**
   * Verifies that all the elements in the actual {@code Object} array belong to the specified type. Matching
   * includes subclasses of the given type.
   * <p>
   * For example, consider the following code listing:
   * <pre>
   * Number[] numbers = { 2, 6 ,8 };
   * assertThat(numbers).hasComponentType(Integer.class);
   * </pre>
   * The assertion <code>hasAllElementsOfType</code> will be successful.
   * </p>
   * @param type the expected type.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the component type of the actual {@code Object} array is not the same as the
   * specified one.
   */
  public ObjectArrayAssert hasAllElementsOfType(Class<?> type) {
    validateIsNotNull(type);
    isNotNull();
    for (Object o : actual) {
      if (type.isInstance(o)) continue;
      failIfCustomMessageIsSet();
      fail(format("not all elements in array:<%s> belong to the type:<%s>", actual, type));
    }
    return this;
  }

  /**
   * Verifies that at least one element in the actual {@code Object} array belong to the specified type. Matching
   * includes subclasses of the given type.
   * @param type the expected type.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Object} does not have any elements of the given type.
   */
  public ObjectArrayAssert hasAtLeastOneElementOfType(Class<?> type) {
    validateIsNotNull(type);
    isNotNull();
    boolean found = false;
    for (Object o : actual) {
      if (!type.isInstance(o)) continue;
      found = true;
      break;
    }
    if (found) return this;
    failIfCustomMessageIsSet();
    throw failure(format("array:<%s> does not have any elements of type:<%s>", actual, type));
  }

  private void validateIsNotNull(Class<?> type) {
    if (type == null) throw new NullPointerException(unexpectedNullType(rawDescription()));
  }

  /**
   * Verifies that the actual {@code Object} array is equal to the given array. Array equality is checked by
   * <code>{@link Arrays#deepEquals(Object[], Object[])}</code>.
   * @param expected the given array to compare the actual array to.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Object} array is not equal to the given one.
   */
  @Override public ObjectArrayAssert isEqualTo(Object[] expected) {
    if (Arrays.deepEquals(actual, expected)) return this;
    failIfCustomMessageIsSet();
    throw failure(unexpectedNotEqual(actual, expected));
  }

  /**
   * Verifies that the actual {@code Object} array is not equal to the given array. Array equality is checked by
   * <code>{@link Arrays#deepEquals(Object[], Object[])}</code>.
   * @param array the given array to compare the actual array to.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Object} array is equal to the given one.
   */
  @Override public ObjectArrayAssert isNotEqualTo(Object[] array) {
    if (!Arrays.deepEquals(actual, array)) return this;
    failIfCustomMessageIsSet();
    throw failure(unexpectedEqual(actual, array));
  }

  /**
   * Creates a new instance of <code>{@link ObjectArrayAssert}</code> whose target array contains the values of the
   * given property name from the elements of this {@code ObjectArrayAssert}'s array. Property access works with both
   * simple properties like {@code Person.age} and nested properties {@code Person.father.age}.
   * </p>
   * <p>
   * For example, let's say we have a array of {@code Person} objects and you want to verify their age:
   * <pre>
   * assertThat(persons).onProperty("age").containsOnly(25, 16, 44, 37); // simple property
   * assertThat(persons).onProperty("father.age").containsOnly(55, 46, 74, 62); // nested property
   * </p>
   * @param propertyName the name of the property to extract values from the actual array to build a new
   * {@code ObjectArrayAssert}.
   * @return a new {@code ObjectArrayAssert} containing the values of the given property name from the elements of this
   * {@code ObjectArrayAssert}'s array.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws IntrospectionError if an element in the given array does not have a matching property.
   * @since 1.3
   */
  @Override public ObjectArrayAssert onProperty(String propertyName) {
    isNotNull();
    if (actual.length == 0) return new ObjectArrayAssert(new Object[0]);
    List<Object> values = PropertySupport.instance().propertyValues(propertyName, list(actual));
    return new ObjectArrayAssert(values.toArray());
  }

  /** {@inheritDoc} */
  @Override protected Set<Object> actualAsSet() {
    return set(actual);
  }

  /** {@inheritDoc} */
  @Override protected List<Object> actualAsList() {
    return list(actual);
  }

  /** {@inheritDoc} */
  @Override protected int actualGroupSize() {
    return actual.length;
  }
}
