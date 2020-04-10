import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminStatsListComponent } from './admin-stats-list.component';

describe('AdminStatsListComponent', () => {
  let component: AdminStatsListComponent;
  let fixture: ComponentFixture<AdminStatsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminStatsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminStatsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
