/**
 *  Copyright 2005-2018 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package com.openshift.evg.roadshow.parks;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.openshift.evg.roadshow.parks.model.Park;
import com.openshift.evg.roadshow.rest.gateway.model.Backend;
import com.openshift.evg.roadshow.rest.gateway.model.Coordinates;

/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@Component
public class Processor {

    public Backend infoBackend() {
    	return new Backend("nationalparks","National Parks", new Coordinates("47.039304", "14.505178"), 4);
    }
    
    public Park parkMapper(Map<String, Object> row) throws SQLException {
		Park park = new Park();
		
		park.setId((Integer)row.get("id"));
		park.setCountryCode(row.get("country_code"));
		park.setCountryName(row.get("country_name"));
		park.setName(row.get("name"));
		park.setToponymName(row.get("toponym_name"));
		park.setLatitude(row.get("latitude"));
		park.setLongitude(row.get("longitude"));
		park.setCountryCode(row.get("country_code"));
		
		com.openshift.evg.roadshow.parks.model.Coordinates pos = new com.openshift.evg.roadshow.parks.model.Coordinates(row.get("latitude").toString(), row.get("longitude").toString());
		
		park.setPosition(pos);
		
		return park;
	}
    
    

}
