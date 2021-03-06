package org.seasar.struts.glue.registry;

import org.seasar.extension.unit.S2TestCase;
import org.seasar.struts.glue.ActionFactory;
import org.seasar.struts.glue.Glue;
import org.seasar.struts.glue.MethodNameExtracter;

public class Seasar2RegistryTest extends S2TestCase {

    @Override
    protected void setUp() {
        include("glue-struts.dicon");
    }

    public void testHasComponent() throws Exception {
        final Seasar2Registry registry = new Seasar2Registry();
        registry.setContainer(getContainer());
        assertTrue(registry.hasComponent("glue"));
        assertTrue(registry.hasComponent("actionFactory"));
        assertTrue(registry.hasComponent("methodNameExtracter"));
    }

    public void testGetComponent() throws Exception {
        final Seasar2Registry registry = new Seasar2Registry();
        registry.setContainer(getContainer());
        final Glue glue = registry.getComponent("glue");
        assertNotNull(glue);
        final ActionFactory actionFactory = registry
                .getComponent("actionFactory");
        assertNotNull(actionFactory);
        final MethodNameExtracter methodNameExtracter = registry
                .getComponent("methodNameExtracter");
        assertNotNull(methodNameExtracter);
    }
}
