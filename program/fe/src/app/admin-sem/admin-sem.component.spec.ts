import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSemComponent } from './admin-sem.component';

describe('AdminSemComponent', () => {
  let component: AdminSemComponent;
  let fixture: ComponentFixture<AdminSemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminSemComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminSemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
