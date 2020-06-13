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

package com.google.sps.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet that returns some example content. TODO: modify this file to handle
 * comments data
 */
@WebServlet("/data")
public final class DataServlet extends HttpServlet {

  private List<String> quotes;
  private List<String> comments;

  @Override
  public void init() {
    comments = new ArrayList<>();
    quotes = new ArrayList<>();
    quotes.add("www.realcollegeboss.com");
    quotes.add("apollohealth.herokuapp.com/");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // String quote = quotes.get((int) (Math.random() * quotes.size()));
    response.setContentType("application/JSON;");
    response.getWriter().println(buildJSON());
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter("comment");
    System.out.println(comment);
    comments.add(comment);
    response.sendRedirect("/index.html");
  }

  private String buildJSON() {
    String json = "{";
    json += "\"Freshman_Summer\": ";
    json += "\"European Bioinformatics Institute\"";
    json += ", ";
    json += "\"Sophomore_Summer\": ";
    json += "\"Google SPS\"";
    json += "}";
    return json;
  }
}
