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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

/**
 * Servlet that returns some example content. TODO: modify this file to handle
 * comments data
 */
@WebServlet("/data")
public final class DataServlet extends HttpServlet {

  @Override
  public void init() {

  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // String quote = quotes.get((int) (Math.random() * quotes.size()));
    // response.setContentType("application/JSON;");
    // response.getWriter().println(buildJSON());
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery results = datastore.prepare(query);
    String languageCode = request.getParameter("code");

    List<comment> comments = new ArrayList<comment>();
    for (Entity entity : results.asIterable()) {

      String title = (String) entity.getProperty("title");
      long timestamp = (long) entity.getProperty("timestamp");
      Translate translate = TranslateOptions.getDefaultInstance().getService();
      Translation translation = translate.translate(title, Translate.TranslateOption.targetLanguage(languageCode));
      String translatedText = translation.getTranslatedText();
      comment comment = new comment(translatedText, timestamp);
      System.out.println(translatedText);
      comments.add(comment);
    }

    Gson gson = new Gson();

    response.setContentType("application/json;");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().println(gson.toJson(comments));
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    String comment = request.getParameter("comment");

    Entity taskEntity = new Entity("Comment");
    long timestamp = System.currentTimeMillis();

    taskEntity.setProperty("title", comment);
    taskEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(taskEntity);

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
