// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    List<Event> sortedEvents = new ArrayList<Event>();
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) { // tests if the duration is longer than the whole day
      return Arrays.asList();
    }
    for (Event event : events) {
      for (String attendent : event.getAttendees()) {
        if (request.getAttendees().contains(attendent)) { // considers events where only the attendents are present
          if (sortedEvents.size() == 0) { // adds first event
            sortedEvents.add(event);
            continue;
          }
          for (int i = 0; i < sortedEvents.size(); i++) {
            if (sortedEvents.get(i).getWhen().overlaps(event.getWhen())) { // will check various overlaps to make sure
                                                                           // to not include overlaps in the list
              Event overlapped;
              if (sortedEvents.get(i).getWhen().start() < event.getWhen().start()) {
                if (sortedEvents.get(i).getWhen().end() > event.getWhen().end()) {
                  overlapped = new Event("Event 1", TimeRange.fromStartEnd(sortedEvents.get(i).getWhen().start(),
                      sortedEvents.get(i).getWhen().end(), false), Collections.emptySet());
                } else {
                  overlapped = new Event("Event 1",
                      TimeRange.fromStartEnd(sortedEvents.get(i).getWhen().start(), event.getWhen().end(), false),
                      Collections.emptySet());
                }
              } else {
                if (sortedEvents.get(i).getWhen().end() > event.getWhen().end()) {
                  overlapped = new Event("Event 1",
                      TimeRange.fromStartEnd(event.getWhen().start(), sortedEvents.get(i).getWhen().end(), false),
                      Collections.emptySet());
                } else {
                  overlapped = new Event("Event 1",
                      TimeRange.fromStartEnd(event.getWhen().start(), event.getWhen().end(), false),
                      Collections.emptySet());
                }
              }
              sortedEvents.remove(sortedEvents.get(i)); // remove the entry previously in the list and add an entry
                                                        // where the events overlap
              sortedEvents.add(overlapped);
              continue;
            }
            sortedEvents.add(event); // if they dont overlap, then we add them

          }
        }
      }
    }
    if (sortedEvents.size() == 0) {
      return Arrays.asList(TimeRange.WHOLE_DAY);
    }
    List<TimeRange> toReturn = new ArrayList<TimeRange>();
    Long lookingFor = request.getDuration();
    for (int i = 0; i < sortedEvents.size(); i++) { // building the algorithm to find slots
      if (i == 0) {
        if (sortedEvents.get(i).getWhen().start() >= lookingFor) {
          toReturn.add(TimeRange.fromStartEnd(0, sortedEvents.get(i).getWhen().start(), false));
        }
      } else if (sortedEvents.get(i).getWhen().start() - sortedEvents.get(i - 1).getWhen().end() >= lookingFor) {
        toReturn.add(TimeRange.fromStartEnd(sortedEvents.get(i - 1).getWhen().end(),
            sortedEvents.get(i).getWhen().start(), false));
      }
      if (i == sortedEvents.size() - 1) {
        if (TimeRange.END_OF_DAY - sortedEvents.get(i).getWhen().end() >= lookingFor) {
          toReturn.add(TimeRange.fromStartEnd(sortedEvents.get(i).getWhen().end(), TimeRange.END_OF_DAY, true));
        }
      }
    }
    return toReturn;
  }
}
