import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminRecrComponent } from './admin-recr.component';

describe('AdminRecrComponent', () => {
  let component: AdminRecrComponent;
  let fixture: ComponentFixture<AdminRecrComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminRecrComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminRecrComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
