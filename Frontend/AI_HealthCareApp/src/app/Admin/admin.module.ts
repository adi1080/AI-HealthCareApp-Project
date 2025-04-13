import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavbarComponent } from './Components/navbar/navbar.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from "../shared/shared.module";
import { AiChatComponentComponent } from '../ai-chat-component/ai-chat-component.component';

const routes: Routes = [
  {
    path: '',
    component: NavbarComponent,
    children: [

    ],
  },
];

@NgModule({
  declarations: [
    NavbarComponent

  ],
  imports: [
    CommonModule, 
    SharedModule,
    RouterModule.forChild(routes),
  ],
  exports: [NavbarComponent],
})
export class AdminModule {}
