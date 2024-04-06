import { Search, MoreHorizontal, ChevronsLeft, ChevronLeft, ChevronRight, ChevronsRight } from 'lucide-react'
import dayjs from 'dayjs'
import 'dayjs/locale/pt-br'
import relativeTime from 'dayjs/plugin/relativeTime'
import { IconButton } from './icon-button'
import { Table } from './table/table'
import { TableHeader } from './table/table-header'
import { TableCell } from './table/table-cell'
import { TableRow } from './table/table-row'
import { ChangeEvent, useEffect, useState } from 'react'

dayjs.extend(relativeTime)
dayjs.locale('pt-br')

interface Attendee {
  id: string
  name: string
  email: string
  createdAt: string
  checkedInAt: string | null
}

export function AttendeeList() {
  const [search, setSearch] = useState('')
  const [page, setPage] = useState(1)
  const [attendees, setAttendees] = useState<Attendee[]>([]);

  const totalPages = Math.ceil(attendees.length / 10)

  useEffect(() => {
    fetch('http://localhost:8080/events/attendees/ef38a5c9-5b46-4796-8fde-90944418108b')
      .then(response => response.json())
      .then(data => {
        setAttendees(data.attendees)
      })
  }, [page])

  function onSearchInputChange(event : ChangeEvent<HTMLInputElement>) {
    setSearch(event.target.value)
    setPage(1)
  }

  function goToFirstPage() {
    setPage(1)
  }

  function goToPreviousPage() {
    setPage(page - 1)
  }

  function goToNextPage() {
    setPage(page + 1)
  }

  function goToLastPage() {
    setPage(totalPages)
  }

  return (
    <div className='flex flex-col gap-4'>
      <div className="flex gap-3 items-center">
        <h1 className="font-bold text-2xl">Participantes</h1>
        <div className="px-3 w-72 py-1.5 border border-white/10 rounded-lg flex items-center gap-3">
          <Search className='size-4 text-emerald-300' />
          <input 
            onChange={onSearchInputChange} 
            className="bg-transparent flex-1 outline-none border-0 p-0 text-sm focus:ring-0" 
            placeholder="Buscar participante..." />
        </div>
      </div>

      <Table>
        <thead>
          <tr className='border-b border-white/10'>
            <TableHeader style={{ width: 48 }}>
              <input type="checkbox" className='size-4 bg-black/20 rounded border border-white/10'/>
            </TableHeader>
            <TableHeader>Código</TableHeader>
            <TableHeader>Participante</TableHeader>
            <TableHeader>Data de inscrição</TableHeader>
            <TableHeader>Data de check-in</TableHeader>
            <TableHeader style={{ width: 64 }}></TableHeader>
          </tr>
        </thead>
        <tbody>
          {attendees
            .filter(attendee => 
              attendee.name.toLowerCase().includes(search.toLowerCase())
              || attendee.email.toLowerCase().includes(search.toLowerCase())
              || attendee.id.includes(search)
            ) 
            .slice((page - 1) * 10, page * 10)
            .map(attendee => {
            return (
              <TableRow key={attendee.id}>
                <TableCell>
                  <input type="checkbox" className='size-4 bg-black/20 rounded border border-white/10'/>
                </TableCell>
                <TableCell>{attendee.id}</TableCell>
                <TableCell>
                  <div className='flex flex-col gap-1'>
                    <span className='font-semibold text-white'>{attendee.name}</span>
                    <span>{attendee.email}</span>
                  </div>
                </TableCell>
                <TableCell>{dayjs().to(attendee.createdAt)}</TableCell>
                <TableCell>
                  {attendee.checkedInAt === null 
                  ? <span className='text-zinc-500'>Não fez check-in</span>
                  : dayjs().to(attendee.checkedInAt)}
                </TableCell>
                <TableCell>
                  <IconButton transparent>
                    <MoreHorizontal className='size-4'/>
                  </IconButton>
                </TableCell>
              </TableRow>
            )
          })} 
        </tbody>
        <tfoot>
            <tr>
              <TableCell colSpan={3}>
                Mostrando {page * 10 < attendees.length ? '10' : attendees.length % 10} de {attendees.length} itens
              </TableCell>
              <TableCell className='text-right' colSpan={3}>
                <div className='inline-flex items-center gap-8'>
                  <span>Página {page} de {totalPages}</span>

                  <div className='flex gap-1.5'>
                    <IconButton onClick={goToFirstPage} disabled={page === 1}>
                      <ChevronsLeft className='size-4'/>
                    </IconButton>
                    <IconButton onClick={goToPreviousPage} disabled={page === 1}>
                      <ChevronLeft className='size-4'/>
                    </IconButton>
                    <IconButton onClick={goToNextPage} disabled={page === totalPages}>
                      <ChevronRight className='size-4'/>
                    </IconButton>
                    <IconButton onClick={goToLastPage} disabled={page === totalPages}>
                      <ChevronsRight className='size-4'/>
                    </IconButton>
                  </div>
                </div>
              </TableCell>
            </tr>
        </tfoot>
      </Table>
    </div>
  )
}