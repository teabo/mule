/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions.internal.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mule.module.extensions.internal.util.IntrospectionUtils.getSetter;
import org.mule.module.extensions.ExtendedPersonalInfo;
import org.mule.module.extensions.HeisenbergExtension;
import org.mule.module.extensions.LifetimeInfo;
import org.mule.module.extensions.internal.capability.metadata.ParameterGroupCapability;
import org.mule.module.extensions.internal.introspection.ParameterGroup;
import org.mule.module.extensions.internal.runtime.resolver.ResolverSetResult;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.size.SmallTest;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class GroupValueSetterTestCase extends AbstractMuleTestCase
{

    private static final String NAME = "name";
    private static final Integer AGE = 50;
    private static final Date DATE = new Date();


    private ValueSetter valueSetter;

    @Mock
    private ResolverSetResult result;

    @Before
    public void before() throws Exception
    {
        ParameterGroup group = new ParameterGroup(ExtendedPersonalInfo.class, getSetter(HeisenbergExtension.class, "personalInfo", ExtendedPersonalInfo.class));
        group.addParameter("myName", getSetter(ExtendedPersonalInfo.class, "myName", String.class));
        group.addParameter("age", getSetter(ExtendedPersonalInfo.class, "age", Integer.class));

        ParameterGroup child = new ParameterGroup(LifetimeInfo.class, getSetter(ExtendedPersonalInfo.class, "lifetimeInfo", LifetimeInfo.class));
        child.addParameter("dateOfBirth", getSetter(LifetimeInfo.class, "dateOfBirth", Date.class));
        group.addCapability(new ParameterGroupCapability(Arrays.asList(child)));

        when(result.get("myName")).thenReturn(NAME);
        when(result.get("age")).thenReturn(AGE);
        when(result.get("dateOfBirth")).thenReturn(DATE);

        valueSetter = new GroupValueSetter(group);
    }

    @Test
    public void set() throws Exception
    {
        HeisenbergExtension extension = new HeisenbergExtension();
        valueSetter.set(extension, result);

        assertThat(extension.getPersonalInfo().getMyName(), is(NAME));
        assertThat(extension.getPersonalInfo().getAge(), is(AGE));
        assertThat(extension.getPersonalInfo().getLifetimeInfo().getDateOfBirth(), is(sameInstance(DATE)));
    }
}
