export interface User {
  username: string;
  email: string;
  employeeId: number;
  token: string;
  userType: string;
}


export interface UserLoginRsp {
  user: User;
}


export interface UserLoginReq {
  user: LoginReq;
}
export interface LoginReq {
  email: string;
  password: string;
  username: string;
}

export interface ForgotPasswordReq {
  email: string;
}
export interface ChangePasswordReq {
  newPassword: string;
  oldPassword: string;
}


export interface GetProfileRsp {
  profile: Profile;
}

export interface Profile {
  birthday: string;
  department: string;
  departmentHistory: DepartmentHistory[];
  email: string;
  emp_name: string;
  firstWorkDate: string;
  gender: string;
  hireDate: string;
  id: number;
  job: string;
  jobHistory: JobHistory[];
  maritalStatus: string;
  permission: string;
  salary: string;
  user_name: string;
}
export interface JobHistory {
  end_date: string;
  new_job: string;
  old_job: string;
  start_date: string;
  username: string;
}
export interface DepartmentHistory {
  end_date: string;
  new_department: string;
  old_department: string;
  start_date: string;
  username: string;
}


export interface PunchReq {
  type: string;
  username: string;
}
export interface PunchRsp {
  username: string;
/*   check_in: string;
  check_out: string; */
  status: string;
  serverTime: string;
}


export interface GetAllBQAttendRsp {
  bqlist: Bqlist[];
}
export interface Bqlist {
  check_in: Checkin;
  check_out: Checkin;
  status: string;
  username: string;
  workdate: string;
  attendanceId: number;
}
export interface Checkin {
  date: number;
  day: number;
  hours: number;
  minutes: number;
  month: number;
  seconds: number;
  time: number;
  timezoneOffset: number;
  year: number;
}


export interface AddEmployeeReq {
  employee: Employee;
  user: LoginReq;
}
export interface Employee {
  birthday: string;
  deptId: number;
  firstWorkDate: string;
  gender: string;
  hireDate: string;
  jobId: number;
  maritalStatus: string;
  name: string;
  permisson: string;
  salary: string;
  workShifts: string;
}


export interface GetPunchMsgRsp {
  check_in: string;
  check_out?: string|null;
  status: string;
  username: string;
}

export interface GetAllProfilesRsp {
  profile: Profile[];
}
export interface GetAllJobsRsp {
  job: Job[];
}
export interface Job {
  id: number;
  jobName: string;
  createdAt: string;
}
export interface GetAllDepartmentsRsp {
  department: Department[];
}
export interface Department {
  id: number;
  deptName: string;
  leaderId: number;
  createdAt: string;
}


export interface PostVacationReq {
  vacationReq: VacationReq;
}
export interface VacationReq {
  days: number;
  endDate: string;
  reason: string;
  startDate: string;
  username: string;
  vocationType: string;
}


export interface GetMyVacationListRsp {
  vocationReqList: VocationReqList[];
}
export interface VocationReqList {
  id: number;
  username: string;
  vocationType: string;
  startDate: string;
  endDate: string;
  days: number;
  reason: string;
  status: string;
}


export interface GetWorkCalendarRsp {
  days: number;
  year: number;
  workCalendar: WorkCalendar[];
}
export interface WorkCalendar {
  date: string;
  dayType: string;
}


export interface BQapplyReq {
  bqapplay: Bqapplay;
}
export interface Bqapplay {
  attendanceId: number;
  username: string;
  status: string;
  workdate: string;
  checkIn: string;
  checkOut: string;
  reason: string;
}


export interface GetVacationRemainingReq {
  vacationinfo: Vacationinfo[];
}
export interface Vacationinfo {
  typename: string;
  limit: boolean;
  remaindays: number;
  evbid: number;
}

export interface GetBQApproveListRsp {
  bqapproveListVo: BqapproveListVo[];
}
export interface BqapproveListVo {
  check_in: Checkin1;
  check_out: Checkin1;
  id: number;
  reason: string;
  status: string;
  username: string;
  workdate: string;
}
export interface Checkin1 {
  hour: number;
  minute: number;
  nano: number;
  second: number;
}

export interface EmployeeUpdateReq {
  birthday: string;
  employeeId: number;
  employeeName: string;
  gender: string;
  hiredDate: string;
  loginName: string;
  permission: string;
  salary: string;
}


export interface GetAllVacationListRsp {
  vocationReqList: VocationReqList[];
}


export interface GetAllScheduleTypeRsp {
 endTime: Checkin;
  shiftName: string;
  startTime: Checkin;

}
export interface AddShiftReq {
  endTime: string;
  shiftName: string;
  startTime: string;
}
export interface GetAllVacationTypeListRsp {
  vacationList: VacationTypeList[];
}
export interface VacationTypeList {
  id: number;
  vacationType: string;
  limitType: string;
}


export interface GetMonthlyAttendanceRsp {
  attendanceRecord: AttendanceRecord[];
  days: number;
  month: number;
}
export interface AttendanceRecord {
  date: string;
  daytype: string;
  status: string;
}


export interface GetToDoRsp {
  todos: Todo[];
}
export interface Todo {
  code: string;
  count: number;
}

// 在您的类型定义文件中
export interface Permissions {
  Vacation_Approve: string;
  Attendance_Approve: string;
  Init_WorkCalendar: string;
  Change_Password: string;
  [key: string]: string; // 添加索引签名
}