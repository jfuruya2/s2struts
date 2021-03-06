select 
  e.empno,
  e.ename,
  e.job,
  e.mgr,
  e.hiredate,
  e.sal,
  e.comm,
  e.deptno,
  d.dname
from 
  emp e 
  left outer join 
  dept d 
  on e.deptno = d.deptno
/*BEGIN*/
where 
/*IF dto.empno != null*/empno = /*dto.empno*/7788/*END*/
/*IF dto.ename != null*/and ename like (/*dto.ename*/'SCOTT' + '%')/*END*/
/*IF dto.job != null*/and job like (/*dto.job*/'ANALYST' + '%')/*END*/
/*IF dto.mgr != null*/and mgr = /*dto.mgr*/7566/*END*/
/*IF dto.fromHiredate != null*/and hiredate >= /*dto.fromHiredate*/'1982-12-01'/*END*/
/*IF dto.toHiredate != null*/and hiredate <= /*dto.toHiredate*/'1982-12-31'/*END*/
/*IF dto.fromSal != null*/and sal >= /*dto.fromSal*/1000/*END*/
/*IF dto.toSal != null*/and sal <= /*dto.toSal*/4000/*END*/
/*IF dto.deptno != null*/and deptno = /*dto.deptno*/20/*END*/
/*END*/