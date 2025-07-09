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

  constructor(private aiService: AiServiceService ,   private cdRef: ChangeDetectorRef // Inject it
    ) {}

  ngOnInit() {
    const sq = localStorage.getItem('userQuery');
    const sr = localStorage.getItem('aiResponse');
    const sl = localStorage.getItem('loadingState');
    if (sq) this.userQuery = sq;
    if (sr) this.response = sr;
    this.loading = sl === 'true';
  }

  onSubmit() {
    const q = this.userQuery.trim();
    if (!q) {
      this.message = 'Please type or speak a question.';
      return;
    }

    this.loading = true;
    this.message = '';
    this.response = "I'm thinking...";
    localStorage.setItem('userQuery', q);
    localStorage.setItem('loadingState', 'true');

    this.aiService.getAiResponse(q).subscribe({
      next: (data: string) => {
        this.loading = false;
        this.response = data;
        localStorage.setItem('aiResponse', data);
        localStorage.setItem('loadingState', 'false');
      },
      error: (err) => {
        this.loading = false;
        console.error('Error:', err);
        this.response = `Error: ${err.message || 'Something went wrong'}`;
        localStorage.setItem('aiResponse', this.response);
        localStorage.setItem('loadingState', 'false');
      }
    });
  }

  startSpeechToText() {
    const recognition = new webkitSpeechRecognition();
    recognition.lang = 'en-US';
    recognition.interimResults = false;
    recognition.maxAlternatives = 1;

    recognition.onstart = () => this.message = '🎤 Listening...';
    recognition.onerror = (e: any) => {
      console.error(e);
      this.message = `Speech error: ${e.error}`;
    };

    recognition.onresult = (e: any) => {
      const transcript = e.results[0][0].transcript;
      this.userQuery = transcript;
      this.cdRef.detectChanges(); // 👈 This will force Angular to update the UI
      this.message = `You said: "${transcript}"`;
      // this.onSubmit(); // auto-submit after speech
    };

    recognition.onend = () => {
      if (!this.message.startsWith('You said')) {
        this.message = '🎤 Speech ended.';
      }
    };

    recognition.start();
  }
}
