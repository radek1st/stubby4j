package io.github.azagniotov.stubby4j.utils;

import io.github.azagniotov.stubby4j.utils.helpers.AnotherType;
import io.github.azagniotov.stubby4j.utils.helpers.SomeType;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SafeGenericsUtilsTest {

    private static final SomeType INSTANCE_SOME_TYPE = new SomeType("8");
    private static final AnotherType INSTANCE_ANOTHER_TYPE = new AnotherType(9L);
    private static final Object RAW_INSTANCE_SOME_TYPE = new SomeType("6");

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void shouldCheckCast() throws Exception {
        SafeGenericsUtils.checkCast(SomeType.class, INSTANCE_SOME_TYPE);
    }

    @Test
    public void shouldThrowWhenCheckCast() throws Exception {
        expectedException.expect(ClassCastException.class);
        expectedException.expectMessage("Expected: io.github.azagniotov.stubby4j.utils.helpers.SomeType, instead got: io.github.azagniotov.stubby4j.utils.helpers.AnotherType");

        SafeGenericsUtils.checkCast(SomeType.class, INSTANCE_ANOTHER_TYPE);
    }

    @Test
    public void shouldCastAs() throws Exception {
        final SomeType casted = SafeGenericsUtils.as(SomeType.class, RAW_INSTANCE_SOME_TYPE);
    }

    @Test
    public void shouldThrowWhenCastAsWrongType() throws Exception {
        expectedException.expect(ClassCastException.class);
        expectedException.expectMessage("Expected: io.github.azagniotov.stubby4j.utils.helpers.AnotherType, instead got: io.github.azagniotov.stubby4j.utils.helpers.SomeType");

        SafeGenericsUtils.as(AnotherType.class, RAW_INSTANCE_SOME_TYPE);
    }

    @Test
    public void shouldThrowWhenCastAsWrongGenericType() throws Exception {
        expectedException.expect(ClassCastException.class);
        expectedException.expectMessage("io.github.azagniotov.stubby4j.utils.helpers.SomeType cannot be cast to io.github.azagniotov.stubby4j.utils.helpers.AnotherType");

        final AnotherType wrongType = SafeGenericsUtils.as(SomeType.class, RAW_INSTANCE_SOME_TYPE);
    }

    @Test
    public void shouldCastCheckedSetWhenRawHomogeneousSet() throws Exception {
        final Set rawHomogeneousSet = new HashSet();
        rawHomogeneousSet.add(INSTANCE_SOME_TYPE);
        rawHomogeneousSet.add(INSTANCE_SOME_TYPE);

        final Set<SomeType> checkedSet = SafeGenericsUtils.asCheckedSet(rawHomogeneousSet, SomeType.class, HashSet::new);
    }

    @Test
    public void shouldThrowWhenCastCheckedSetWhenRawHeterogeneousSet() throws Exception {
        expectedException.expect(ClassCastException.class);

        final Set rawHeterogeneousSet = new HashSet();
        rawHeterogeneousSet.add(new SomeType("alex"));
        rawHeterogeneousSet.add(INSTANCE_ANOTHER_TYPE);
        rawHeterogeneousSet.add(INSTANCE_SOME_TYPE);
        rawHeterogeneousSet.add(new AnotherType(999L));

        final Set<SomeType> checkedSet = SafeGenericsUtils.asCheckedSet(rawHeterogeneousSet, SomeType.class, HashSet::new);
    }

    @Test
    public void shouldCastCheckedListWhenRawHomogeneousList() throws Exception {
        final List rawHomogeneousList = new ArrayList();
        rawHomogeneousList.add(new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));
        rawHomogeneousList.add(new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));

        final List<SomeType> checkedList = SafeGenericsUtils.asCheckedList(rawHomogeneousList, HashSet.class, ArrayList::new);
    }

    @Test
    public void shouldThrowWhenCastCheckedListWhenRawHomogeneousListWithWrongValueClass() throws Exception {
        expectedException.expect(ClassCastException.class);

        final List rawHomogeneousList = new ArrayList();
        rawHomogeneousList.add(new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));
        rawHomogeneousList.add(new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));

        final List<SomeType> checkedList = SafeGenericsUtils.asCheckedList(rawHomogeneousList, SomeType.class, ArrayList::new);
    }

    @Test
    public void shouldThrowWhenCastCheckedListWhenRawHeterogeneousList() throws Exception {
        expectedException.expect(ClassCastException.class);

        final List rawHomogeneousList = new ArrayList();
        rawHomogeneousList.add(INSTANCE_SOME_TYPE);
        rawHomogeneousList.add(INSTANCE_ANOTHER_TYPE);

        final List<SomeType> checkedList = SafeGenericsUtils.asCheckedList(rawHomogeneousList, AnotherType.class, ArrayList::new);
    }

    @Test
    public void shouldCastCheckedListWhenRawHomogeneousCollection() throws Exception {
        final Collection rawHomogeneousCollection = new ArrayList();
        rawHomogeneousCollection.add(new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));
        rawHomogeneousCollection.add(new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));

        final Collection<SomeType> checkedList = SafeGenericsUtils.asCheckedCollection(rawHomogeneousCollection, HashSet.class, ArrayList::new);
    }

    @Test
    public void shouldThrowWhenCastCheckedListWhenRawHomogeneousCollectionWithWrongValueClass() throws Exception {
        expectedException.expect(ClassCastException.class);

        final Collection rawHomogeneousCollection = new ArrayList();
        rawHomogeneousCollection.add(new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));
        rawHomogeneousCollection.add(new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));

        final Collection<SomeType> checkedList = SafeGenericsUtils.asCheckedCollection(rawHomogeneousCollection, SomeType.class, ArrayList::new);
    }

    @Test
    public void shouldThrowWhenCastCheckedListWhenRawHeterogeneousCollection() throws Exception {
        expectedException.expect(ClassCastException.class);

        final Collection rawHomogeneousCollection = new ArrayList();
        rawHomogeneousCollection.add(INSTANCE_SOME_TYPE);
        rawHomogeneousCollection.add(INSTANCE_ANOTHER_TYPE);

        final Collection<SomeType> checkedList = SafeGenericsUtils.asCheckedCollection(rawHomogeneousCollection, AnotherType.class, ArrayList::new);
    }

    @Test
    public void shouldCastCheckedMapWhenRawHomogeneousMap() throws Exception {
        final Map rawHomogeneousMap = new HashMap();
        rawHomogeneousMap.put(INSTANCE_SOME_TYPE.getValue(), new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));
        rawHomogeneousMap.put(INSTANCE_SOME_TYPE.getValue(), new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));

        final Map<String, SomeType> checkedMap = SafeGenericsUtils.asCheckedMap(rawHomogeneousMap, String.class, HashSet.class, HashMap::new);
    }

    @Test
    public void shouldThrowWhenCastCheckedMapWhenRawHomogeneousMapWithWrongValueClass() throws Exception {
        expectedException.expect(ClassCastException.class);

        final Map rawHomogeneousMap = new HashMap();
        rawHomogeneousMap.put(INSTANCE_SOME_TYPE.getValue(), new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));
        rawHomogeneousMap.put(INSTANCE_SOME_TYPE.getValue(), new HashSet<>(Collections.singletonList(INSTANCE_SOME_TYPE)));

        final Map<String, SomeType> checkedMap = SafeGenericsUtils.asCheckedMap(rawHomogeneousMap, String.class, SomeType.class, HashMap::new);
    }

    @Test
    public void shouldThrowWhenCastCheckedMapWhenRawHeterogeneousMap() throws Exception {
        expectedException.expect(ClassCastException.class);

        final Map rawHeterogeneousMap = new HashMap();
        rawHeterogeneousMap.put(INSTANCE_SOME_TYPE.getValue(), INSTANCE_SOME_TYPE);
        rawHeterogeneousMap.put(INSTANCE_ANOTHER_TYPE.getValue(), INSTANCE_ANOTHER_TYPE);

        final Map<String, SomeType> checkedMap = SafeGenericsUtils.asCheckedMap(rawHeterogeneousMap, String.class, SomeType.class, LinkedHashMap::new);
    }
}