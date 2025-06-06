import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from 'src/app/Services/user.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit{

  constructor(private userService: UserService, public fb: FormBuilder , private router:Router) { }

  UserForm!: FormGroup;

  ngOnInit(): void {
    this.UserForm = this.fb.group({
      username: [],  // Match the formControlName="username"
      password: [],
      emailid: [],
      role: []       // Match the formControlName="role"
    });
  }

  onSubmit() {
    console.log(this.UserForm.value);  // Log the form values for debugging
    this.userService.addUser(this.UserForm.value).subscribe(
      response => {
        console.log('User registered successfully', response);
      },
      error => {
        console.error('Error registering user', error);
      }
    );
    // this.UserForm.reset();
    this.router.navigateByUrl("/login")
  }
}
