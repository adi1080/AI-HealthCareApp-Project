import { Component } from '@angular/core';
import { Router } from '@angular/router';

declare var bootstrap: any; 

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {
  constructor(private router:Router){ }
    
  ngOnInit(): void {

  }

  logout(){
    this.router.navigateByUrl('login');
  }

}
