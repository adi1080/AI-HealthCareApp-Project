import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-pre-search',
  templateUrl: './pre-search.component.html',
  styleUrls: ['./pre-search.component.scss']
})
export class PreSearchComponent {

  constructor(private router: Router) { }

  navigateToSearch() {
    this.router.navigateByUrl("patient/search");
  }
}
