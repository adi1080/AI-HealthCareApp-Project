import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MessageService } from 'src/app/Services/message.service';
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
    private userService: UserService,
    private messageService:MessageService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });

    if (!this.userService.isLoggedIn()) {
      this.userService.removeToken();
    }
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.userService.login(this.loginForm.value).subscribe(
        (response: any) => {
          console.log(response);

          // ✅ Token received — user permitted
          if (response.token) {
            this.userService.storeToken(response.token);

            const userInfo = this.userService.getUserInfoFromToken();

            if (!userInfo) {
              this.messageService.showMessage('Invalid token data', 3000);
              return;
            }

            // Navigate based on role
            if (userInfo.role === 'Doctor') {
              localStorage.setItem('DoctorUserId', userInfo.id);
              this.router.navigateByUrl('/doctor');
            } else if (userInfo.role === 'User') {
              localStorage.setItem('PatientUserId', userInfo.id);
              this.router.navigateByUrl('/patient');
            } else if (userInfo.role === 'Admin') {
              this.router.navigateByUrl('/admin');
            } else {
              this.messageService.showMessage('Unknown user role', 3000);
            }
          } else {
            this.messageService.showMessage('No token received', 3000);
          }
        },
        (error) => {
          console.error('Login failed', error);

          if (error.status === 403 && error.error === 'Your account has been blocked. Please contact admin.') {
            this.messageService.showMessage('You have been blocked temporarily due to misconduct.', 4000);
          }
          else if (error.status === 401 && error.error === 'Invalid password') {
            this.messageService.showMessage('Invalid password.', 3000);
          }
          else {
            this.messageService.showMessage('Invalid login details.', 3000);
          }
        }
      );
    }
  }


  teleport() {
    this.router.navigateByUrl('Register');
  }
}
