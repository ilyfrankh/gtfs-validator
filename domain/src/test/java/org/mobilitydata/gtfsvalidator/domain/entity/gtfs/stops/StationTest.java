/*
 * Copyright (c) 2020. MobilityData IO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mobilitydata.gtfsvalidator.domain.entity.gtfs.stops;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mobilitydata.gtfsvalidator.domain.entity.gtfs.EntityBuildResult;
import org.mobilitydata.gtfsvalidator.domain.entity.notice.error.MissingRequiredValueNotice;
import org.mobilitydata.gtfsvalidator.domain.entity.notice.error.UnexpectedEnumValueNotice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mobilitydata.gtfsvalidator.domain.entity.notice.base.Notice.KEY_ENUM_VALUE;
import static org.mobilitydata.gtfsvalidator.domain.entity.notice.base.Notice.KEY_FIELD_NAME;

// Fields annotated `@NonNull` but test require it. Therefore suppressing lint
@SuppressWarnings("ConstantConditions")
class StationTest {
    private static final String STOP_ID = "stop_id";
    private static final String STOP_NAME = "stop_name";
    private static final float STOP_LAT = 45.5017f;
    private static final float STOP_LON = 73.5673f;
    private static final String STOP_CODE = "stop_code";
    private static final String STOP_DESC = "stop_desc";
    private static final String ZONE_ID = "zone_id";
    private static final String STOP_URL = "stop_url";
    private static final String STOP_TIMEZONE = "stop_timezone";
    private static final String WHEELCHAIR_BOARDING = "wheelchair_boarding";
    private static final String LEVEL_ID = "level_id";

    private static final Station.StationBuilder underTest = new Station.StationBuilder();

    @BeforeEach
    public void clearBuilder() {
        underTest.clear();
    }

    @Test
    public void createStationWithMinimizedValidDataShouldNotGenerateNotice() {
        underTest.wheelchairBoarding(null)
                .levelId(null)
                .stopId(STOP_ID)
                .stopName(STOP_NAME)
                .stopLat(STOP_LAT)
                .stopLon(STOP_LON)
                .stopCode(null)
                .stopDesc(null)
                .zoneId(null)
                .stopUrl(null)
                .stopTimezone(null)
                .childrenList(null);

        final EntityBuildResult<?> buildResult = underTest.build();

        assertTrue(buildResult.getData() instanceof Station);

        final Station toCheck = (Station) buildResult.getData();

        assertNull(toCheck.getLevelId());
        assertEquals(WheelchairBoarding.UNKNOWN_WHEELCHAIR_BOARDING, toCheck.getWheelchairBoarding());
        assertEquals(STOP_ID, toCheck.getStopId());
        assertEquals(STOP_NAME, toCheck.getStopName());
        assertEquals(STOP_LAT, toCheck.getStopLat());
        assertEquals(STOP_LON, toCheck.getStopLon());
        assertNull(toCheck.getStopCode());
        assertNull(toCheck.getStopDesc());
        assertNull(toCheck.getZoneId());
        assertNull(toCheck.getStopUrl());
        assertNull(toCheck.getStopTimezone());
        assertNull(toCheck.getChildren());
    }

    @Test
    public void createStationWithMaximizedValidDataShouldNotGenerateNotice() {
        underTest.wheelchairBoarding(2)
                .levelId(LEVEL_ID)
                .stopId(STOP_ID)
                .stopName(STOP_NAME)
                .stopLat(STOP_LAT)
                .stopLon(STOP_LON)
                .stopCode(STOP_CODE)
                .stopDesc(STOP_DESC)
                .zoneId(ZONE_ID)
                .stopUrl(STOP_URL)
                .stopTimezone(STOP_TIMEZONE)
                .childrenList(new ArrayList<>());

        final EntityBuildResult<?> buildResult = underTest.build();

        assertTrue(buildResult.getData() instanceof Station);

        final Station toCheck = (Station) buildResult.getData();

        assertEquals(WheelchairBoarding.NOT_WHEELCHAIR_ACCESSIBLE, toCheck.getWheelchairBoarding());
        assertEquals(LEVEL_ID, toCheck.getLevelId());
        assertEquals(STOP_ID, toCheck.getStopId());
        assertEquals(STOP_NAME, toCheck.getStopName());
        assertEquals(STOP_LAT, toCheck.getStopLat());
        assertEquals(STOP_LON, toCheck.getStopLon());
        assertEquals(STOP_CODE, toCheck.getStopCode());
        assertEquals(STOP_DESC, toCheck.getStopDesc());
        assertEquals(ZONE_ID, toCheck.getZoneId());
        assertEquals(STOP_URL, toCheck.getStopUrl());
        assertEquals(STOP_TIMEZONE, toCheck.getStopTimezone());
        assertEquals(Collections.emptyList(), toCheck.getChildren());
    }

    @Test
    public void createStationWithNullStopIdShouldGenerateMissingRequiredValueNotice() {
        underTest.wheelchairBoarding(2)
                .levelId(LEVEL_ID)
                .stopId(null)
                .stopName(STOP_NAME)
                .stopLat(STOP_LAT)
                .stopLon(STOP_LON)
                .stopCode(STOP_CODE)
                .stopDesc(STOP_DESC)
                .zoneId(ZONE_ID)
                .stopUrl(STOP_URL)
                .stopTimezone(STOP_TIMEZONE)
                .childrenList(null);

        final EntityBuildResult<?> entityBuildResult = underTest.build();
        assertTrue(entityBuildResult.getData() instanceof ArrayList);

        // to avoid lint regarding cast, the test is designed so that method .getData() returns a list of notices.
        //noinspection unchecked
        final List<MissingRequiredValueNotice> noticeCollection =
                (List<MissingRequiredValueNotice>) entityBuildResult.getData();
        final MissingRequiredValueNotice notice = noticeCollection.get(0);

        assertEquals("stops.txt", notice.getFilename());
        assertEquals(STOP_ID, notice.getNoticeSpecific(KEY_FIELD_NAME));
        assertEquals("no id", notice.getEntityId());
        assertEquals(1, noticeCollection.size());
    }

    @Test
    public void createStationWithNullStopNameShouldGenerateMissingRequiredValueNotice() {
        underTest.wheelchairBoarding(2)
                .levelId(LEVEL_ID)
                .stopId(STOP_ID)
                .stopName(null)
                .stopLat(STOP_LAT)
                .stopLon(STOP_LON)
                .stopCode(STOP_CODE)
                .stopDesc(STOP_DESC)
                .zoneId(ZONE_ID)
                .stopUrl(STOP_URL)
                .stopTimezone(STOP_TIMEZONE)
                .childrenList(null);

        final EntityBuildResult<?> entityBuildResult = underTest.build();
        assertTrue(entityBuildResult.getData() instanceof ArrayList);

        // to avoid lint regarding cast, the test is designed so that method .getData() returns a list of notices.
        //noinspection unchecked
        final List<MissingRequiredValueNotice> noticeCollection =
                (List<MissingRequiredValueNotice>) entityBuildResult.getData();
        final MissingRequiredValueNotice notice = noticeCollection.get(0);

        assertEquals("stops.txt", notice.getFilename());
        assertEquals(STOP_NAME, notice.getNoticeSpecific(KEY_FIELD_NAME));
        assertEquals(STOP_ID, notice.getEntityId());
        assertEquals(1, noticeCollection.size());
    }

    @Test
    public void createStationWithNullStopLatShouldGenerateMissingRequiredValueNotice() {
        underTest.wheelchairBoarding(2)
                .levelId(LEVEL_ID)
                .stopId(STOP_ID)
                .stopName(STOP_NAME)
                .stopLat(null)
                .stopLon(STOP_LON)
                .stopCode(STOP_CODE)
                .stopDesc(STOP_DESC)
                .zoneId(ZONE_ID)
                .stopUrl(STOP_URL)
                .stopTimezone(STOP_TIMEZONE)
                .childrenList(null);

        final EntityBuildResult<?> entityBuildResult = underTest.build();
        assertTrue(entityBuildResult.getData() instanceof ArrayList);

        // to avoid lint regarding cast, the test is designed so that method .getData() returns a list of notices.
        //noinspection unchecked
        final List<MissingRequiredValueNotice> noticeCollection =
                (List<MissingRequiredValueNotice>) entityBuildResult.getData();
        final MissingRequiredValueNotice notice = noticeCollection.get(0);

        assertEquals("stops.txt", notice.getFilename());
        assertEquals("stop_lat", notice.getNoticeSpecific(KEY_FIELD_NAME));
        assertEquals(STOP_ID, notice.getEntityId());
        assertEquals(1, noticeCollection.size());
    }

    @Test
    public void createStationWithNullStopLonShouldGenerateMissingRequiredValueNotice() {
        underTest.wheelchairBoarding(2)
                .levelId(LEVEL_ID)
                .stopId(STOP_ID)
                .stopName(STOP_NAME)
                .stopLat(STOP_LAT)
                .stopLon(null)
                .stopCode(STOP_CODE)
                .stopDesc(STOP_DESC)
                .zoneId(ZONE_ID)
                .stopUrl(STOP_URL)
                .stopTimezone(STOP_TIMEZONE)
                .childrenList(null);

        final EntityBuildResult<?> entityBuildResult = underTest.build();
        assertTrue(entityBuildResult.getData() instanceof ArrayList);

        // to avoid lint regarding cast, the test is designed so that method .getData() returns a list of notices.
        //noinspection unchecked
        final List<MissingRequiredValueNotice> noticeCollection =
                (List<MissingRequiredValueNotice>) entityBuildResult.getData();
        final MissingRequiredValueNotice notice = noticeCollection.get(0);

        assertEquals("stops.txt", notice.getFilename());
        assertEquals("stop_lon", notice.getNoticeSpecific(KEY_FIELD_NAME));
        assertEquals(STOP_ID, notice.getEntityId());
        assertEquals(1, noticeCollection.size());
    }

    @Test
    public void createStationWithInvalidWheelchairBoardingShouldGenerateUnexpectedEnumValueNotice() {
        underTest.wheelchairBoarding(5)
                .levelId(LEVEL_ID)
                .stopId(STOP_ID)
                .stopName(STOP_NAME)
                .stopLat(STOP_LAT)
                .stopLon(STOP_LON)
                .stopCode(STOP_CODE)
                .stopDesc(STOP_DESC)
                .zoneId(ZONE_ID)
                .stopUrl(STOP_URL)
                .stopTimezone(STOP_TIMEZONE)
                .childrenList(null);

        final EntityBuildResult<?> entityBuildResult = underTest.build();
        assertTrue(entityBuildResult.getData() instanceof ArrayList);

        // to avoid lint regarding cast, the test is designed so that method .getData() returns a list of notices.
        //noinspection unchecked
        final List<UnexpectedEnumValueNotice> noticeCollection =
                (List<UnexpectedEnumValueNotice>) entityBuildResult.getData();
        final UnexpectedEnumValueNotice notice = noticeCollection.get(0);

        assertEquals("stops.txt", notice.getFilename());
        assertEquals(WHEELCHAIR_BOARDING, notice.getNoticeSpecific(KEY_FIELD_NAME));
        assertEquals(STOP_ID, notice.getEntityId());
        assertEquals(5, notice.getNoticeSpecific(KEY_ENUM_VALUE));
    }
}