import { Component, OnInit } from '@angular/core';
import { AiServiceService } from '../Services/ai-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ai-chat-component',
  templateUrl: './ai-chat-component.component.html',
  styleUrls: ['./ai-chat-component.component.scss']
})
export class AiChatComponentComponent implements OnInit {
  userQuery: string = ''; // For capturing user input
  response: string = ''; // To display the response from AI
  loading!: boolean;

  constructor(private aiService: AiServiceService, private router: Router) {}

  ngOnInit() {
    // Check if there is saved data in localStorage
    var savedQuery = localStorage.getItem('userQuery');
    var savedResponse = localStorage.getItem('aiResponse');
    var savedLoading = localStorage.getItem('loadingState');

    if (savedQuery && savedResponse) {
      this.userQuery = savedQuery;
      this.response = savedResponse;
      this.loading = savedLoading === 'true';
    }

  }

  onSubmit() {
    this.loading = true;
    this.response = "I'm thinking..."; // Immediate placeholder response

    // Save the query in localStorage
    localStorage.setItem('userQuery', this.userQuery);
    localStorage.setItem('loadingState', 'true');

    if (this.userQuery.trim()) {
      this.aiService.getAiResponse(this.userQuery).subscribe(
        (data: string) => {
          this.loading = false;
          this.response = data;  // Update the response with AI's reply
          // Save the AI response to localStorage
          localStorage.setItem('aiResponse', data);
          localStorage.setItem('loadingState', 'false');
        },
        (error) => {
          this.loading = false;
          console.error('Error details:', error);
          this.response = `Error: ${error.message || 'Unable to get response from the AI'}`;
          // Save the error in localStorage
          localStorage.setItem('aiResponse', this.response);
          localStorage.setItem('loadingState', 'false');
        }
      );
    }
  }

}
