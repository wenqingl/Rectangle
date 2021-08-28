/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import javax.print.DocFlavor.STRING;
import javax.sql.DataSource;
import javax.swing.text.Document;

import java.lang.ProcessBuilder.Redirect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@SpringBootApplication
public class Main {

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Autowired
  private DataSource dataSource;

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @RequestMapping("/") //show button and current database
  String index(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rectangle (id serial, name varchar(20), width integer, height integer, color varchar(20), borderWidth integer)");
      ResultSet rs = stmt.executeQuery("SELECT * FROM rectangle");
      ArrayList<Rectangle> output = new ArrayList<Rectangle>();

      while (rs.next()) {
        Rectangle temp = new Rectangle();
        temp.setId(rs.getInt("id"));
        temp.setName(rs.getString("name"));
        temp.setColor(rs.getString("color"));
        temp.setWidth(rs.getInt("width"));
        temp.setHeight(rs.getInt("height"));
        temp.setBorderWidth(rs.getInt("borderWidth"));
        output.add(temp);
      } 

      model.put("rectangles", output);
      return "index";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @GetMapping(
    path = "/addRectangle"
  )
  public String getRectangleForm(Map<String, Object> model){
    Rectangle rectangle = new Rectangle();
    model.put("rectangle", rectangle);
    return "addRectangle";
  }

  @PostMapping(
    path = "/addRectangle", 
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String handleBrowserRectangleSubmit(Map<String, Object> model, Rectangle rectangle) throws Exception{
    //save the data into the database
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS rectangle (id serial, name varchar(20), width integer, height integer, color varchar(20), borderWidth integer)");
      String output = "INSERT INTO rectangle (name,width,height,color,borderWidth) VALUES ('" + rectangle.getName() + "','" + rectangle.getWidth() + "','" + rectangle.getHeight() + "','" + rectangle.getColor() + "','" + rectangle.getBorderWidth() + "')";
      stmt.executeUpdate(output);

      System.out.println(rectangle.getName());
      return "redirect:/rectangle/success";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
    
  }

  @GetMapping("/rectangle/success")
  public String getRectangleSuccess(){
    return "success";
  }

  @GetMapping(
    path = "/deleteRectangle"
  )
  public String deleteRectangleForm(Map<String, Object> model, Rectangle rectangle){
    return "deleteRectangle";
  }

  @PostMapping(
    path = "/deleteRectangle",
    consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
  )
  public String DeleteRectangle(Map<String, Object> model, Rectangle rectangle) throws Exception{
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      int idNum = rectangle.getId();
      System.out.println(idNum);
      String output = "DELETE FROM rectangle WHERE id=" + idNum;
      stmt.executeUpdate(output);
      
      return "deleteSuccessfully";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
    
  }

  @GetMapping(
    path = "/deleteRectangle/deleteSuccessfully"
  )
  public String deleteRectangleSuccessfully(Map<String, Object> model){
    return "deleteSuccessfully";
  }


  @GetMapping("/view/{pid}")
  public String viewRectangle(Map<String, Object> model, @PathVariable String pid){
    try(Connection connection = dataSource.getConnection()){
      Statement stat = connection.createStatement();
      String sql = "SELECT * FROM rectangle WHERE id=" + pid;
      ResultSet rs = stat.executeQuery(sql);
      
      while(rs.next()){
      Rectangle temp = new Rectangle();
      temp.setName(rs.getString("name"));
      temp.setWidth(rs.getInt("width"));
      temp.setHeight(rs.getInt("height"));
      temp.setColor(rs.getString("color"));
      temp.setBorderWidth(rs.getInt("borderWidth"));
      temp.setId(rs.getInt("id"));
      model.put("view", temp);
      }

      return "view";

    }catch(Exception e){
      model.put("message", e.getMessage());
      return "error";
    }
    
  }

  @PostMapping("/")
  public String BackHome(Map<String, Object> model){
    return "redirect:/";
  }

  @RequestMapping("/db")
  String db(Map<String, Object> model) {
    try (Connection connection = dataSource.getConnection()) {
      Statement stmt = connection.createStatement();
      stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
      stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
      ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");

      ArrayList<String> output = new ArrayList<String>();
      while (rs.next()) {
        output.add("Read from DB: " + rs.getTimestamp("tick"));
      }

      model.put("records", output);
      return "db";
    } catch (Exception e) {
      model.put("message", e.getMessage());
      return "error";
    }
  }

  @Bean
  public DataSource dataSource() throws SQLException {
    if (dbUrl == null || dbUrl.isEmpty()) {
      return new HikariDataSource();
    } else {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(dbUrl);
      return new HikariDataSource(config);
    }
  }

}
