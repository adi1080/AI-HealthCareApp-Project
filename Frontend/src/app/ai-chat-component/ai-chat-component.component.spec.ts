import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AiChatComponentComponent } from './ai-chat-component.component';

describe('AiChatComponentComponent', () => {
  let component: AiChatComponentComponent;
  let fixture: ComponentFixture<AiChatComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AiChatComponentComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AiChatComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
