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

import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;

import com.openshift.evg.roadshow.parks.model.Park;
import com.openshift.evg.roadshow.rest.gateway.model.Backend;
import com.openshift.evg.roadshow.rest.gateway.model.Coordinates;

/**
 * A spring-boot application that includes a Camel route builder to setup the Camel routes
 */
@Component
public class ListStrategy implements AggregationStrategy {

    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (oldExchange == null) {
        	List<Park> parkList = new ArrayList<Park>();
            parkList.add(newExchange.getIn().getBody(Park.class));
            newExchange.getIn().setBody(parkList);
            
            return newExchange;
        }
        
        List<Park> parkList = oldExchange.getIn().getBody(List.class);
        parkList.add(newExchange.getIn().getBody(Park.class));
        
        return oldExchange;
    }
}
