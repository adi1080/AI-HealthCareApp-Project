import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AiServiceService } from '../Services/ai-service.service';

declare var webkitSpeechRecognition: any;

@Component({
  selector: 'app-ai-chat-component',
  templateUrl: './ai-chat-component.component.html',
  styleUrls: ['./ai-chat-component.component.scss']
})
export class AiChatComponentComponent implements OnInit {
  userQuery = '';
  response = '';
  loading = false;
  message = '';
  isLoggedIn = false;

  constructor(private aiService: AiServiceService, private cdRef: ChangeDetectorRef) { }

  ngOnInit() {
    const session = localStorage.getItem('sessionActive');
    if (session === 'true') {
      this.isLoggedIn = true;
      this.userQuery = localStorage.getItem('userQuery') || '';
      this.response = localStorage.getItem('aiResponse') || '';
      this.loading = localStorage.getItem('loadingState') === 'true';
    }
  }

  onSubmit() {
    const q = this.userQuery.trim();
    if (!q) {
      this.message = 'Please type or speak a question.';
      return;
    }

    this.loading = true;
    this.response = '';
    this.message = 'Responding...';
    localStorage.setItem('sessionActive', 'true');
    localStorage.setItem('userQuery', q);
    localStorage.setItem('loadingState', 'true');

    let lastLength = 0;  // Track length of the last received token string

    this.aiService.getAiResponseStream(q).subscribe({
      next: (token: string) => {
        if (token === '__END__') {
          this.loading = false;
          localStorage.setItem('loadingState', 'false');
          return;
        }

        // Append only the new part after lastLength if token is cumulative
        if (token.length > lastLength) {
          this.response += token.substring(lastLength);
          lastLength = token.length;
        }

        localStorage.setItem('aiResponse', this.response);
        this.cdRef.detectChanges();
        this.message = "";
      },
      error: (err) => {
        this.loading = false;
        if (!this.response) {
          this.response = 'Error receiving AI response' + err;
        }
        this.message = "";
        localStorage.setItem('aiResponse', this.response);
        localStorage.setItem('loadingState', 'false');
        this.cdRef.detectChanges();
      }
    });
  }

  startSpeechToText() {
    if (!('webkitSpeechRecognition' in window)) {
      this.message = 'Speech recognition not supported in this browser.';
      return;
    }

    const recognition = new webkitSpeechRecognition();
    recognition.lang = 'en-US';
    recognition.interimResults = false;
    recognition.maxAlternatives = 1;

    let speechCaptured = false;

    recognition.onstart = () => {
      this.message = '🎤 Listening...';
      this.cdRef.detectChanges();
    };

    recognition.onerror = (e: any) => {
      console.error('Speech error:', e);
      this.message = `Speech error: ${e.error}`;
      recognition.stop();
      this.cdRef.detectChanges();
    };

    recognition.onresult = (e: any) => {
      const transcript = e.results[0][0].transcript;
      this.userQuery = transcript;
      speechCaptured = true;
      this.message = `You said: "${transcript}"`;
      this.cdRef.detectChanges();
    };

    recognition.onend = () => {
      if (!speechCaptured) {
        this.message = '🎤 No speech detected or cancelled.';
        this.cdRef.detectChanges();
      }
    };

    recognition.start();
  }
}
