import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgressView } from './progress-view';

describe('ProgressView', () => {
  let component: ProgressView;
  let fixture: ComponentFixture<ProgressView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgressView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProgressView);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
