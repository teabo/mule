/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions.internal.capability.xml;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mule.module.extensions.internal.capability.xml.schema.SchemaDocumenterPostProcessor.EXTENSION_ELEMENT;
import static org.mule.module.extensions.internal.capability.xml.schema.SchemaDocumenterPostProcessor.PROCESSING_ENVIRONMENT;
import org.mule.extensions.introspection.api.ExtensionBuilder;
import org.mule.extensions.introspection.api.ExtensionDescribingContext;
import org.mule.module.extensions.internal.capability.xml.schema.SchemaDocumenterPostProcessor;
import org.mule.module.extensions.internal.introspection.NavigableExtensionBuilder;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.size.SmallTest;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class SchemaDocumenterPostProcessorTestCase extends AbstractMuleTestCase
{

    @Mock
    private ExtensionDescribingContext context;

    @Mock
    private NavigableExtensionBuilder builder;

    private Map<String, Object> parameters;

    private SchemaDocumenterPostProcessor postProcessor;

    @Before
    public void before()
    {
        when(context.getExtensionBuilder()).thenReturn(builder);
        parameters = spy(new HashMap<String, Object>());
        parameters.put(EXTENSION_ELEMENT, mock(TypeElement.class));
        parameters.put(PROCESSING_ENVIRONMENT, mock(ProcessingEnvironment.class));

        when(context.getCustomParameters()).thenReturn(parameters);

        postProcessor = new SchemaDocumenterPostProcessor();
    }

    @Test
    public void noNavigableBuilder()
    {
        when(context.getExtensionBuilder()).thenReturn(mock(ExtensionBuilder.class));
        postProcessor.postProcess(context);

        verify(parameters, never()).get(anyString());
    }

    @Test
    public void noProcessingEnvironment()
    {
        parameters.remove(PROCESSING_ENVIRONMENT);
        postProcessor.postProcess(context);

        verifyZeroInteractions(builder);
    }

    @Test
    public void noExtensionElement()
    {
        parameters.remove(EXTENSION_ELEMENT);
        postProcessor.postProcess(context);

        verifyZeroInteractions(builder);
    }

}
