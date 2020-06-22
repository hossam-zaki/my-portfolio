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

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings = [
    "Here's to the crazy ones",
    "The Misfits",
    "The Rebels",
    "The round pegs in the square holes",
  ];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById("greeting-container");
  greetingContainer.innerText = greeting;
}

function getComments() {
  var e = document.getElementById("languages");
  var strCode = e.options[e.selectedIndex].value;
  fetch("/data?code=" + strCode)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      const statsListElement = document.getElementById("comment-container");
      statsListElement.innerHTML = "";
      data.forEach((comment) => {
        console.log(comment);
        statsListElement.appendChild(createListElement(comment));
      });
    });
}
/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement("li");
  liElement.innerText = text.title;
  return liElement;
}
