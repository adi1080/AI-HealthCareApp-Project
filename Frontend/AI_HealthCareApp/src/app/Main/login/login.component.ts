import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/Services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  loginForm!: FormGroup;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  showMessage(message: string, duration: number) {
    // Create overlay background
    const overlayDiv = document.createElement('div');
    overlayDiv.className = `
    position-fixed top-0 start-0 w-100 h-100
    bg-dark bg-opacity-50 d-flex justify-content-center align-items-center
    fade show
  `;
    overlayDiv.style.zIndex = '1050'; // Like Bootstrap modals

    // Create message card
    const popup = document.createElement('div');
    popup.className = `
    card text-center shadow border-primary animate__animated animate__fadeIn
  `;
    popup.style.width = '22rem';
    popup.style.zIndex = '1060';
    popup.style.position = 'relative';

    // Header with close button
    const header = document.createElement('div');
    header.className =
      'card-header bg-primary text-white d-flex justify-content-between align-items-center';

    const title = document.createElement('span');
    title.textContent = 'Message';

    const closeButton = document.createElement('button');
    closeButton.innerHTML = '&times;';
    closeButton.className = 'btn-close btn-close-white';
    closeButton.style.fontSize = '1.4rem';
    closeButton.onclick = () => {
      overlayDiv.remove();
    };

    header.appendChild(title);
    header.appendChild(closeButton);

    // Body with message
    const body = document.createElement('div');
    body.className = 'card-body';

    const messageEl = document.createElement('p');
    messageEl.textContent = message;
    messageEl.className = 'card-text fs-5 fw-semibold text-dark';
    body.appendChild(messageEl);

    popup.appendChild(header);
    popup.appendChild(body);
    overlayDiv.appendChild(popup);
    document.body.appendChild(overlayDiv);

    // Auto-dismiss after duration
    setTimeout(() => {
      if (overlayDiv.parentNode) {
        overlayDiv.remove();
      }
    }, duration);
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.userService.login(this.loginForm.value).subscribe(
        (response: any) => {
          if (response.token) {
            this.userService.storeToken(response.token);

            const userInfo = this.userService.getUserInfoFromToken();

            if (!userInfo) {
              this.showMessage('Invalid token data', 3000);
              return;
            }

            // Optional: if you still want to store userId separately
            if (userInfo.role === 'Doctor') {
              localStorage.setItem('DoctorUserId', userInfo.id);
              this.router.navigateByUrl('/doctor');
            } else if (userInfo.role === 'User') {
              localStorage.setItem('PatientUserId', userInfo.id);
              this.router.navigateByUrl('/patient');
            } else if (userInfo.role === 'Admin') {
              this.router.navigateByUrl('/admin');
            } else {
              this.showMessage('Unknown user role', 3000);
            }
          } else {
            this.showMessage('No token received', 3000);
          }
        },
        (error) => {
          console.error('Login failed', error);
          this.showMessage('Invalid credentials', 3000);
        }
      );
    }
  }

  teleport() {
    this.router.navigateByUrl('Register');
  }
}
