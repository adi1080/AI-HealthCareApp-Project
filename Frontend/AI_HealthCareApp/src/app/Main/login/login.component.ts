import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/Services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
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
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      this.userService.login(this.loginForm.value).subscribe(
        (response: string) => {
          // Redirect based on the response
          if (response === "admin") {
            this.router.navigateByUrl('/admin');
          } else if (response === "patient") {
            this.router.navigateByUrl('/patient');
          } else if (response.startsWith("doctor")) {
            const userId = response.replace('doctor', '');
            // Store the user ID in localStorage
            localStorage.setItem('userId', userId);

            this.router.navigateByUrl('/doctor');
          }
        },
        error => {
          console.error('Login failed', error);
          alert('Invalid credentials');
        }
      );
    }
  }
  
  teleport() {
    this.router.navigateByUrl("Register");
  }
}
